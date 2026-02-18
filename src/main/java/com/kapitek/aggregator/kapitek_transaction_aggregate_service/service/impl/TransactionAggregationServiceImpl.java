package com.kapitek.aggregator.kapitek_transaction_aggregate_service.service.impl;

import org.springframework.stereotype.Service;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.dto.AggregateRequestDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.dto.AggregateResponseDTO;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.service.TransactionAggregationService;

@Service
public class TransactionAggregationServiceImpl implements TransactionAggregationService {

    @Override
    public AggregateResponseDTO getRecentTransactionsSummary(String accountNumber, int months) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public AggregateResponseDTO getRecentCategorizeAggregatedTransactions(String accountNumber, int months) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AggregateResponseDTO geTransactionsSummaryBetweenDates(AggregateRequestDTO aggregateRequestDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public AggregateResponseDTO geAggregatedCategorizedTransactionsBetweenDates(AggregateRequestDTO aggregateRequestDTO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
