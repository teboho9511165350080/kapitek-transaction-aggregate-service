package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.input.rest;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.request.AggregateRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.AggregateResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.input.TransactionAggregationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get aggregated transactions for a customer",
            description = "Returns the categorized and aggregated transactions for a given customer key over the last N months."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggregated transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AggregateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/account/{customerInfoFileKey}/aggregated/months/{months}")
    public ResponseEntity<AggregateResponse> getRecentAggregatedTransactions(
            @PathVariable String customerInfoFileKey,
            @PathVariable int months) {

        AggregateResponse response = transactionAggregationUseCase.getRecentCategorizeAggregatedTransactions(
                customerInfoFileKey, months);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get aggregated transaction summary for a customer",
            description = "Returns a summary of transactions for a given customer key number over the last N months."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggregated summary retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AggregateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/account/{customerKey}/aggregated-summary/months/{months}")
    public ResponseEntity<AggregateResponse> getRecentAggregatedSummaryTransactions(
            @PathVariable String customerKey,
            @PathVariable int months) {

        AggregateResponse response = transactionAggregationUseCase.getRecentTransactionsSummary(customerKey, months);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get aggregated transaction summary between dates",
            description = "Returns the summarized transactions for a customer over a specified date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggregated summary retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AggregateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/aggregated-summary")
    public ResponseEntity<AggregateResponse> getRecentAggregatedSummaryTransactionsBetweenDates(
            @RequestBody AggregateRequest aggregateRequest) {

        AggregateResponse response = transactionAggregationUseCase.geTransactionsSummaryBetweenDates(aggregateRequest);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get aggregated transaction summary between dates",
            description = "Returns the summarized transactions for a customer over a specified date range."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aggregated summary retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AggregateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/aggregated")
    public ResponseEntity<AggregateResponse> getRecentAggregatedTransactionsBetweenDates(
            @RequestBody AggregateRequest aggregateRequest) {

        AggregateResponse response = transactionAggregationUseCase.geAggregatedCategorizedTransactionsBetweenDates(aggregateRequest);

        return ResponseEntity.ok(response);
    }
}
