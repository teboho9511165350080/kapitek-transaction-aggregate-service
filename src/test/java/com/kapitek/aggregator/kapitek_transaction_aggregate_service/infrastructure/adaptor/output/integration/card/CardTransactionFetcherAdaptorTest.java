package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.Cards;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.CustomerInfoFile;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.CustomerInfoPort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CardTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.client.CardTransactionFeignClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.config.CardAuthProperties;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.dto.CardTransactionDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.dto.CardTransactionRequestDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.token.KeycloakTokenClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata.CardTransactionTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardTransactionFetcherAdaptorTest {

    @Mock
    private CustomerInfoPort customerInfoPort;

    @Mock
    private CardTransactionFeignClient feignClient;

    @Mock
    private KeycloakTokenClient tokenClient;

    @Mock
    private CardAuthProperties authProperties;

    @InjectMocks
    private CardTransactionFetcherAdaptor cardTransactionFetcherAdaptor;

    @Test
    void shouldFetchAndMapTransactionsFromBothCards() {

        String customerInfoFileKey = "TEB 001";

        CustomerInfoFile customerInfoFile = mock(CustomerInfoFile.class);
        Cards cards = mock(Cards.class);

        CardTransactionDTO cardTransactionDTO = CardTransactionTestData.getDebitCardTransactionDTO(
                "100.50", "2.50",  "3110", "Pick & Pay Grocery");

        commonMocks();

        when(customerInfoPort.getFileLinkedToCustomerInfoKey(customerInfoFileKey)).thenReturn(customerInfoFile);
        when(customerInfoFile.getCards()).thenReturn(cards);
        when(cards.getCreditCardNumber()).thenReturn(cardTransactionDTO.getCardNumber());
        when(cards.getPrePaidCardNumber()).thenReturn(cardTransactionDTO.getCardNumber());


        when(feignClient.getCardTransactions(anyMap(), any(CardTransactionRequestDTO.class)))
                .thenReturn(List.of(cardTransactionDTO));

        List<CardTransaction> result = cardTransactionFetcherAdaptor.fetchTransactions(customerInfoFileKey);

        ArgumentCaptor<Map<String, String>> headerCaptor = ArgumentCaptor.forClass(Map.class);

        verify(feignClient, times(2)).getCardTransactions(headerCaptor.capture(), any());

        Map<String, String> headers = headerCaptor.getValue();
        assertThat(headers.get("Authorization")).isEqualTo("Bearer access-token");

        assertThat(result).hasSize(2);

        CardTransaction tx = result.getFirst();

        assertThat(tx.getCustomerInfoFileKey()).isEqualTo(customerInfoFileKey);
        assertThat(tx.getTransactionId()).isEqualTo(cardTransactionDTO.getRetrievalReferenceNumber());
        assertThat(tx.getCardNumber()).isEqualTo(cardTransactionDTO.getCardNumber());
        assertThat(tx.getDirection()).isEqualTo(Direction.fromValue(cardTransactionDTO.getDirection()));
        assertThat(tx.getAmount()).hasToString(cardTransactionDTO.getAmount());
        assertThat(tx.getFee()).hasToString(cardTransactionDTO.getPaymentFee());
        assertThat(tx.getDescription()).isEqualTo(cardTransactionDTO.getReference());
        assertThat(tx.getTransactionDate()).isEqualTo(cardTransactionDTO.getTransactionDate());
    }


    @Test
    void shouldReturnEmptyListWhenCustomerFileIsNull() {

        when(customerInfoPort.getFileLinkedToCustomerInfoKey("TEB 001"))
                .thenReturn(null);

        List<CardTransaction> result = cardTransactionFetcherAdaptor.fetchTransactions("TEB 001");

        assertThat(result).isEmpty();
        verifyNoInteractions(feignClient);
    }

    @Test
    void shouldReturnEmptyWhenCardsAreNull() {

        CustomerInfoFile file = mock(CustomerInfoFile.class);

        when(customerInfoPort.getFileLinkedToCustomerInfoKey("TEB 001"))
                .thenReturn(file);
        when(file.getCards()).thenReturn(null);

        List<CardTransaction> result = cardTransactionFetcherAdaptor.fetchTransactions("TEB 001");

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