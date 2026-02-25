package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.input;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.request.AggregateRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.AggregateResponse;

public interface TransactionAggregationUseCase {
    AggregateResponse getRecentTransactionsSummary(String customerInfoFileKey, int months);

    AggregateResponse getRecentCategorizeAggregatedTransactions(String customerInfoFileKey, int months);

    AggregateResponse geTransactionsSummaryBetweenDates(AggregateRequest aggregateRequest);
    
    AggregateResponse geAggregatedCategorizedTransactionsBetweenDates(AggregateRequest aggregateRequest);
}
