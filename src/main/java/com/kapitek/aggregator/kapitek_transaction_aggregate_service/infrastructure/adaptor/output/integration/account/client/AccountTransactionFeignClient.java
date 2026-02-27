package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.client;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.dto.AccountTransactionDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.dto.AccountTransactionRequestDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.config.FeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "kapitek-account-transactions-feign-client",
        url = "${kapitek.datasource.transaction.account.url}",
        configuration =  FeignErrorDecoder.class
)
public interface AccountTransactionFeignClient {
    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    List<AccountTransactionDTO> getAccountTransactions(
            @RequestHeader Map<String, String> header,
            @RequestBody AccountTransactionRequestDTO accountTransactionRequestDTO
    );
}
