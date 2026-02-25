package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.input.web;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.request.AggregateRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.AggregateResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.input.TransactionAggregationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions/")
public class AggregateCategorizedTransactionController {

    private final TransactionAggregationUseCase transactionAggregationUseCase;

    @GetMapping("/account/{customerInfoFileKey}/aggregated/months/{months}")
    public ResponseEntity<AggregateResponse> getRecentAggregatedTransactions(
            @PathVariable String customerInfoFileKey,
            @PathVariable int months) {

        AggregateResponse response = transactionAggregationUseCase.getRecentCategorizeAggregatedTransactions(
                customerInfoFileKey, months);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountNumber}/aggregated-summary/months/{months}")
    public ResponseEntity<AggregateResponse> getRecentAggregatedSummaryTransactions(
            @PathVariable String accountNumber,
            @PathVariable int months) {

        AggregateResponse response = transactionAggregationUseCase.getRecentTransactionsSummary(accountNumber, months);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/aggregated-summary")
    public ResponseEntity<AggregateResponse> getRecentAggregatedSummaryTransactionsBetweenDates(
            @RequestBody AggregateRequest aggregateRequest) {

        AggregateResponse response = transactionAggregationUseCase.geTransactionsSummaryBetweenDates(aggregateRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/aggregated")
    public ResponseEntity<AggregateResponse> getRecentAggregatedTransactionsBetweenDates(
            @RequestBody AggregateRequest aggregateRequest) {

        AggregateResponse response = transactionAggregationUseCase.geAggregatedCategorizedTransactionsBetweenDates(aggregateRequest);

        return ResponseEntity.ok(response);
    }
}
