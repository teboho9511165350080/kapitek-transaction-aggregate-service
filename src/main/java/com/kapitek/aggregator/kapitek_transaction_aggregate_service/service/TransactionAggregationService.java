package com.kapitek.aggregator.kapitek_transaction_aggregate_service.service;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.dto.AggregateRequestDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.dto.AggregateResponseDTO;

public interface TransactionAggregationService {
    AggregateResponseDTO getRecentTransactionsSummary(String accountNumber, int months);

    AggregateResponseDTO getRecentCategorizeAggregatedTransactions(String accountNumber, int months);

    AggregateResponseDTO geTransactionsSummaryBetweenDates(AggregateRequestDTO aggregateRequestDTO);
    
    AggregateResponseDTO geAggregatedCategorizedTransactionsBetweenDates(AggregateRequestDTO aggregateRequestDTO);
}
