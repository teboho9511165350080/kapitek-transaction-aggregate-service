package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.configuration;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.client.AccountTransactionFeignClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.client.CardTransactionFeignClient;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.client.CustomerInfoFileFeignClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(
        basePackageClasses = {
                AccountTransactionFeignClient.class,
                CardTransactionFeignClient.class,
                CustomerInfoFileFeignClient.class
        }
)
public class FeignClientConfiguration {
}
