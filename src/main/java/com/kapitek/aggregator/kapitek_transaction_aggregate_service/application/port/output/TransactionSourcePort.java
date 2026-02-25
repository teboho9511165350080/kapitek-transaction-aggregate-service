package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;

import java.util.List;

public interface TransactionSourcePort <T extends Transaction> {
    List<T> fetchTransactions(String accountNumber);
}
