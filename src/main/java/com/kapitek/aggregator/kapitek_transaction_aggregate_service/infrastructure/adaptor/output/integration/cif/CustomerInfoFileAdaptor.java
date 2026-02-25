package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.CustomerInfoFile;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.CustomerInfoPort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.client.CustomerInfoFileFeignClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.config.CifAuthProperties;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.dto.CustomerInfoFileResponseDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.token.KeycloakTokenClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerInfoFileAdaptor implements CustomerInfoPort {

    private static final String BEARER = "Bearer ";

    private final CustomerInfoFileFeignClient customerInfoFileFeignClient;

    private final KeycloakTokenClient keycloakTokenClient;

    private final CifAuthProperties cifAuthProperties;

    @Override
    public CustomerInfoFile getFileLinkedToCustomerInfoKey(String customerInfoFileKey) {
        CustomerInfoFileResponseDTO customerInfo = customerInfoFileFeignClient.getCustomerInfoFileByKey(
                getHeaders(), customerInfoFileKey);
        return CustomerInfoFileMapper.mapCustomerInfoFile(customerInfo);
    }

    private Map<String, String> getHeaders() {
        TokenResponse tokenResponse = keycloakTokenClient.getClientCredentialsToken(getTokenRequest());
        return Map.of(HttpHeaders.AUTHORIZATION, BEARER.concat(tokenResponse.getAccessToken()));
    }

    private TokenRequest getTokenRequest() {
        return TokenRequest.builder()
                .clientId(cifAuthProperties.getClientId())
                .clientSecret(cifAuthProperties.getClientSecret())
                .grantType(cifAuthProperties.getGrantType())
                .tokenUrl(cifAuthProperties.getUrl())
                .build();
    }
}
