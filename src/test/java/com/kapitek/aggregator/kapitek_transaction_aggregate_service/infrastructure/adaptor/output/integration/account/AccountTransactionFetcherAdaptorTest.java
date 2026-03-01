package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.Accounts;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.CustomerInfoFile;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.CustomerInfoPort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.AccountTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.client.AccountTransactionFeignClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.config.AccountAuthProperties;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.dto.AccountTransactionDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.dto.AccountTransactionRequestDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.token.KeycloakTokenClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata.AccountTransactionTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountTransactionFetcherAdaptorTest {

    @Mock
    private CustomerInfoPort customerInfoPort;

    @Mock
    private AccountTransactionFeignClient feignClient;

    @Mock
    private KeycloakTokenClient tokenClient;

    @Mock
    private AccountAuthProperties authProperties;

    @InjectMocks
    private AccountTransactionFetcherAdaptor accountTransactionFetcherAdaptor;

    @Test
    void shouldFetchAndMapTransactionsFromBothAccounts() {

        String customerInfoFileKey = "TEB 001";

        CustomerInfoFile customerInfoFile = mock(CustomerInfoFile.class);
        Accounts accounts = mock(Accounts.class);

        AccountTransactionDTO accountTransactionDTO = AccountTransactionTestData.getDebitAccountTransactionDTO(
                "100.50", "2.50", "External Payshap Payment");

        commonMocks();

        when(customerInfoPort.getFileLinkedToCustomerInfoKey(customerInfoFileKey)).thenReturn(customerInfoFile);
        when(customerInfoFile.getAccounts()).thenReturn(accounts);
        when(accounts.getChequeAccountNumber()).thenReturn(accountTransactionDTO.getAccountNbr());


        when(feignClient.getAccountTransactions(anyMap(), any(AccountTransactionRequestDTO.class)))
                .thenReturn(List.of(accountTransactionDTO));

        List<AccountTransaction> result = accountTransactionFetcherAdaptor.fetchTransactions(customerInfoFileKey);

        ArgumentCaptor<Map<String, String>> headerCaptor = ArgumentCaptor.forClass(Map.class);

        verify(feignClient, times(2)).getAccountTransactions(headerCaptor.capture(), any());

        Map<String, String> headers = headerCaptor.getValue();
        assertThat(headers.get("Authorization")).isEqualTo("Bearer access-token");

        assertThat(result).hasSize(2);

        AccountTransaction tx = result.getFirst();

        assertThat(tx.getCustomerInfoFileKey()).isEqualTo(customerInfoFileKey);
        assertThat(tx.getTransactionId()).isEqualTo(accountTransactionDTO.getReferenceId());
        assertThat(tx.getAccountNumber()).isEqualTo(accountTransactionDTO.getAccountNbr());
        assertThat(tx.getDirection()).isEqualTo(Direction.fromValue(accountTransactionDTO.getDirection()));
        assertThat(tx.getAmount()).hasToString(accountTransactionDTO.getTxAmount());
        assertThat(tx.getFee()).hasToString(accountTransactionDTO.getTxCharge());
        assertThat(tx.getDescription()).isEqualTo(accountTransactionDTO.getReference());
        assertThat(tx.getTransactionDate()).isEqualTo(accountTransactionDTO.getTxDate());
    }

    @Test
    void shouldReturnEmptyListWhenCustomerFileIsNull() {

        when(customerInfoPort.getFileLinkedToCustomerInfoKey("TEB 001"))
                .thenReturn(null);

        List<AccountTransaction> result = accountTransactionFetcherAdaptor.fetchTransactions("TEB 001");

        assertThat(result).isEmpty();
        verifyNoInteractions(feignClient);
    }

    @Test
    void shouldReturnEmptyWhenAccountsAreNull() {

        CustomerInfoFile file = mock(CustomerInfoFile.class);

        when(customerInfoPort.getFileLinkedToCustomerInfoKey("TEB 001"))
                .thenReturn(file);
        when(file.getAccounts()).thenReturn(null);

        List<AccountTransaction> result = accountTransactionFetcherAdaptor.fetchTransactions("TEB 001");

        assertThat(result).isEmpty();
        verifyNoInteractions(feignClient);
    }

    private void commonMocks() {
        when(authProperties.getClientId()).thenReturn("client-id");
        when(authProperties.getClientSecret()).thenReturn("secret");
        when(authProperties.getGrantType()).thenReturn("client_credentials");
        when(authProperties.getUrl()).thenReturn("http://token-url");

        TokenResponse tokenResponse = mock(TokenResponse.class);
        when(tokenResponse.getAccessToken()).thenReturn("access-token");
        when(tokenClient.getClientCredentialsToken(any())).thenReturn(tokenResponse);
    }
}