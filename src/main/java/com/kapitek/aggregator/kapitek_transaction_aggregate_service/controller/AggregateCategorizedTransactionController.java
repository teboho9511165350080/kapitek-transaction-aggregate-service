package com.kapitek.aggregator.kapitek_transaction_aggregate_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.dto.AggregateResponseDTO;

@RestController
@RequestMapping("/transactions/")
public class AggregateCategorizedTransactionController {

    private final Transaction

    @GetMapping("/account/{accountNumber}/aggregated/months/{months}")
    public ResponseEntity<AggregateResponseDTO> getRecentAggregatedTransactions(
            @PathVariable String accountNumber,
            @PathVariable int months) {

        AggregateResponseDTO response = aggregationService.getRecentAggregatedTransactions(accountNumber, months);

        return ResponseEntity.ok(response);
    }

}
