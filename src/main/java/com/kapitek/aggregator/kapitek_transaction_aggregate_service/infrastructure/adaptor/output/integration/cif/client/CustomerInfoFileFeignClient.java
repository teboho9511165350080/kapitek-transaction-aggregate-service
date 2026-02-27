package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.client;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.dto.CustomerInfoFileResponseDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.configuration.FeignClientGlobalConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(
        name = "kapitek-customer-info-file-feign-client",
        url = "${kapitek.customer.info.url}",
        configuration = FeignClientGlobalConfiguration.class
)
public interface CustomerInfoFileFeignClient {

    @GetMapping("/key/{customerInfoFileKey}")
    CustomerInfoFileResponseDTO getCustomerInfoFileByKey(
            @RequestHeader Map<String, String> headers,
            @PathVariable String customerInfoFileKey
    );
}
