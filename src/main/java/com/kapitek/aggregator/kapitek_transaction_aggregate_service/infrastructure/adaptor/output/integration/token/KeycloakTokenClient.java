package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.token;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.dto.TokenResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.token.enums.TokenConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KeycloakTokenClient {

    private final RestTemplate restTemplate;

    public TokenResponse getClientCredentialsToken(TokenRequest tokenRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(TokenConstants.GRANT_TYPE, tokenRequest.getGrantType());
        body.add(TokenConstants.CLIENT_ID, tokenRequest.getClientId());
        body.add(TokenConstants.CLIENT_SECRET, tokenRequest.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<TokenResponse> response =
                restTemplate.postForEntity(tokenRequest.getTokenUrl(), request, TokenResponse.class);

        return response.getBody();
    }

}
