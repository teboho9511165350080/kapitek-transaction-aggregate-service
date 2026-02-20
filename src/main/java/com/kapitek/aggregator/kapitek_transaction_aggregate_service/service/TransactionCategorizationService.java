package com.kapitek.aggregator.kapitek_transaction_aggregate_service.service;

public interface TransactionCategorizationService <T extends Transaction> {
    CategorizedTransaction categorizedTransaction(T transaction);
}
