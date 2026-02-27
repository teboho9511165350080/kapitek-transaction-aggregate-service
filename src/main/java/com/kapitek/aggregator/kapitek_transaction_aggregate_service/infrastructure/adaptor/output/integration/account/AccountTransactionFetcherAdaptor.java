package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.CustomerInfoFile;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.CustomerInfoPort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.TransactionSourcePort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.AccountTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.client.AccountTransactionFeignClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.config.AccountAuthProperties;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.dto.AccountTransactionDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.dto.AccountTransactionRequestDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.token.KeycloakTokenClient;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Component("accountTransactionFetcher")
public class AccountTransactionFetcherAdaptor implements TransactionSourcePort<AccountTransaction> {

    private static final String BEARER = "Bearer ";

    private final CustomerInfoPort customerInfoPort;

    private final AccountTransactionFeignClient accountTransactionFeignClient;

    private final KeycloakTokenClient keycloakTokenClient;

    private final AccountAuthProperties accountAuthProperties;

    @Override
    public List<AccountTransaction> fetchTransactions(String customerInfoFileKey) {

        List<AccountTransactionDTO> allCustomerAccountsTransactions = new ArrayList<>();

            CustomerInfoFile customerInfoFile = customerInfoPort.getFileLinkedToCustomerInfoKey(customerInfoFileKey);

        if (Objects.nonNull(customerInfoFile) && Objects.nonNull(customerInfoFile.getAccounts())) {
            String chequeAccountNumber = customerInfoFile.getAccounts().getChequeAccountNumber();
            String savingsAccountNumber = customerInfoFile.getAccounts().getChequeAccountNumber();

            addTransactions(chequeAccountNumber, allCustomerAccountsTransactions);
            addTransactions(savingsAccountNumber, allCustomerAccountsTransactions);
        }

        return allCustomerAccountsTransactions.stream()
                .map(transaction->mapToAccountTransaction(transaction, customerInfoFileKey))
                .toList();
    }

    private void addTransactions(String accountNumber, List<AccountTransactionDTO> allCustomerAccountsTransactions) {
        if (StringUtils.isNotBlank(accountNumber)) {
            List<AccountTransactionDTO> accountTransactions = accountTransactionFeignClient.getAccountTransactions(
                    getHeaders(), getAccountTransactionRequestDTO(accountNumber));
            allCustomerAccountsTransactions.addAll(accountTransactions);
        }
    }

    private Map<String, String> getHeaders() {
        TokenResponse tokenResponse = keycloakTokenClient.getClientCredentialsToken(getTokenRequest());
        return Map.of(HttpHeaders.AUTHORIZATION, BEARER.concat(tokenResponse.getAccessToken()));
    }

    private TokenRequest getTokenRequest() {
        return TokenRequest.builder()
                .clientId(accountAuthProperties.getClientId())
                .clientSecret(accountAuthProperties.getClientSecret())
                .grantType(accountAuthProperties.getGrantType())
                .tokenUrl(accountAuthProperties.getUrl())
                .build();
    }

    private AccountTransactionRequestDTO getAccountTransactionRequestDTO(String accountNumber) {
        return AccountTransactionRequestDTO.builder()
                .accountNumber(accountNumber)
                .build();
    }

    private AccountTransaction mapToAccountTransaction(AccountTransactionDTO accountTransactionDTO, String customerInfoKey) {
        return AccountTransaction.builder()
                .customerInfoFileKey(customerInfoKey)
                .transactionId(accountTransactionDTO.getReferenceId())
                .accountNumber(accountTransactionDTO.getAccountNbr())
                .direction(Direction.fromValue(accountTransactionDTO.getDirection()))
                .amount(new BigDecimal(accountTransactionDTO.getTxAmount()))
                .fee(new BigDecimal(accountTransactionDTO.getTxCharge()))
                .description(accountTransactionDTO.getReference())
                .transactionDate(accountTransactionDTO.getTxDate())
                .build();
    }
}
