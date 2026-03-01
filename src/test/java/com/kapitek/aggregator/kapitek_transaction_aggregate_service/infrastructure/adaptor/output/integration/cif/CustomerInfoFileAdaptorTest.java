package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.CustomerInfoFile;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.client.CustomerInfoFileFeignClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.config.CifAuthProperties;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.dto.AccountsDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.dto.CardsDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.dto.CustomerInfoFileResponseDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.token.KeycloakTokenClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerInfoFileAdaptorTest {

    @Mock
    private CustomerInfoFileFeignClient feignClient;

    @Mock
    private KeycloakTokenClient tokenClient;

    @Mock
    private CifAuthProperties authProperties;

    @InjectMocks
    private CustomerInfoFileAdaptor customerInfoFileAdaptor;

    @BeforeEach
    void setUp() {
        commonMocks();
    }

    @Test
    void shouldFetchAndMapCustomerInfoFile() {
        String customerInfoFileKey = "TEB 001";

        CustomerInfoFileResponseDTO customerInfoFileResponseDTO = new CustomerInfoFileResponseDTO();
        customerInfoFileResponseDTO.setCustomerInfoFileKey(customerInfoFileKey);
        customerInfoFileResponseDTO.setAccounts(AccountsDTO.builder().build());
        customerInfoFileResponseDTO.setCards(CardsDTO.builder().build());

        when(feignClient.getCustomerInfoFileByKey(anyMap(), eq(customerInfoFileKey)))
                .thenReturn(customerInfoFileResponseDTO);

        CustomerInfoFile result = customerInfoFileAdaptor.getFileLinkedToCustomerInfoKey(customerInfoFileKey);

        ArgumentCaptor<Map<String, String>> headerCaptor = ArgumentCaptor.forClass(Map.class);
        verify(feignClient).getCustomerInfoFileByKey(headerCaptor.capture(), eq(customerInfoFileKey));

        Map<String, String> headers = headerCaptor.getValue();
        assertThat(headers.get("Authorization")).isEqualTo("Bearer access-token");

        assertThat(result).isNotNull();
        assertThat(result.getCustomerInfoFileKey()).isEqualTo(customerInfoFileKey);
    }

    @Test
    void shouldBuildCorrectTokenRequest() {
        CustomerInfoFileResponseDTO customerInfoFileResponseDTO = new CustomerInfoFileResponseDTO();
        customerInfoFileResponseDTO.setCustomerInfoFileKey("TEB 001");
        customerInfoFileResponseDTO.setAccounts(AccountsDTO.builder().build());
        customerInfoFileResponseDTO.setCards(CardsDTO.builder().build());

        when(feignClient.getCustomerInfoFileByKey(anyMap(), eq("TEB 001")))
                .thenReturn(customerInfoFileResponseDTO);

        customerInfoFileAdaptor.getFileLinkedToCustomerInfoKey("TEB 001");

        ArgumentCaptor<?> tokenRequestCaptor = ArgumentCaptor.forClass(Object.class);
        verify(tokenClient, times(1)).getClientCredentialsToken((TokenRequest) tokenRequestCaptor.capture());
        Object tokenRequest = tokenRequestCaptor.getValue();

        assertThat(tokenRequest).isNotNull();
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