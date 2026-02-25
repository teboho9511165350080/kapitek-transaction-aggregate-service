package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.client;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.dto.CardTransactionDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.dto.CardTransactionRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "kapitek-card-transactions-feign-client",
        url = "${kapitek.datasource.transaction.card.url}"
)
public interface CardTransactionFeignClient {
    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    List<CardTransactionDTO> getCardTransactions(
            @RequestHeader Map<String, String> header,
            @RequestBody CardTransactionRequestDTO cardTransactionRequestDTO
    );
}
