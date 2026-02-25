package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kapitek.datasource.transaction.card.auth")
public class CardAuthProperties {

    private String url;
    private String clientId;
    private String clientSecret;
    private String grantType;
}