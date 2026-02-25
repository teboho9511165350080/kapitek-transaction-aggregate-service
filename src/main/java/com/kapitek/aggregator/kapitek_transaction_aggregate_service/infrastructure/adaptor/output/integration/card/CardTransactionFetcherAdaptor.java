package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.CustomerInfoFile;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.CustomerInfoPort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.TransactionSourcePort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CardTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.client.CardTransactionFeignClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.config.CardAuthProperties;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.dto.CardTransactionDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.dto.CardTransactionRequestDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.token.KeycloakTokenClient;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component("cardTransactionFetcher")
public class CardTransactionFetcherAdaptor implements TransactionSourcePort<CardTransaction> {

    private static final String BEARER = "Bearer ";

    private final CardTransactionFeignClient cardTransactionFeignClient;

    private final KeycloakTokenClient keycloakTokenClient;

    private final CardAuthProperties cardAuthProperties;

    private final CustomerInfoPort customerInfoPort;

    @Override
    public List<CardTransaction> fetchTransactions(String customerInfoFileKey) {

        List<CardTransactionDTO> allCustomerCardsTransactions = new ArrayList<>();
        CustomerInfoFile customerInfoFile = customerInfoPort.getFileLinkedToCustomerInfoKey(customerInfoFileKey);

        if (Objects.nonNull(customerInfoFile) && Objects.nonNull(customerInfoFile.getCards())) {
            String creditCardNumber = customerInfoFile.getCards().getCreditCardNumber();
            String prePaidCardNumber = customerInfoFile.getCards().getPrePaidCardNumber();

            addTransactions(creditCardNumber, allCustomerCardsTransactions);
            addTransactions(prePaidCardNumber, allCustomerCardsTransactions);
        }

        return allCustomerCardsTransactions.stream()
                .map(transaction->mapToCardTransaction(transaction, customerInfoFileKey))
                .toList();
    }

    private void addTransactions(String cardNumber, List<CardTransactionDTO> allCustomerAccountsTransactions) {
        if (StringUtils.isNotBlank(cardNumber)) {
            List<CardTransactionDTO> cardTransactions = cardTransactionFeignClient.getCardTransactions(
                    getHeaders(), getCardTransactionRequestDTO(cardNumber));
            allCustomerAccountsTransactions.addAll(cardTransactions);
        }
    }

    private Map<String, String> getHeaders() {
        TokenResponse tokenResponse = keycloakTokenClient.getClientCredentialsToken(getTokenRequest());
        return Map.of(HttpHeaders.AUTHORIZATION, BEARER.concat(tokenResponse.getAccessToken()));
    }

    private TokenRequest getTokenRequest() {
        return TokenRequest.builder()
                .clientId(cardAuthProperties.getClientId())
                .clientSecret(cardAuthProperties.getClientSecret())
                .grantType(cardAuthProperties.getGrantType())
                .tokenUrl(cardAuthProperties.getUrl())
                .build();
    }

    private CardTransactionRequestDTO getCardTransactionRequestDTO(String cardNumber) {
        return CardTransactionRequestDTO.builder()
                .cardNumber(cardNumber)
                .build();
    }

    private CardTransaction mapToCardTransaction(CardTransactionDTO cardTransactionDTO, String customerInfoFileKey) {
        return CardTransaction.builder()
                .customerInfoFileKey(customerInfoFileKey)
                .transactionId(cardTransactionDTO.getRetrievalReferenceNumber())
                .cardNumber(cardTransactionDTO.getCardNumber())
                .direction(Direction.fromValue(cardTransactionDTO.getDirection()))
                .amount(new BigDecimal(cardTransactionDTO.getAmount()))
                .fee(new BigDecimal(cardTransactionDTO.getPaymentFee()))
                .description(cardTransactionDTO.getReference())
                .transactionDate(cardTransactionDTO.getTransactionDate())
                .mcc(cardTransactionDTO.getMerchantCategoryCode())
                .build();
    }
}
