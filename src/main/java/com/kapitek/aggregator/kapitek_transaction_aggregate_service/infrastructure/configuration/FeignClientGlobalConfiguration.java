package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.configuration;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.config.FeignErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class FeignClientGlobalConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}