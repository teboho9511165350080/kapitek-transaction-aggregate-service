package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.categorize;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;

public interface TransactionCategorizationService  {
    String UNCATEGORIZED = "Uncategorized";

    CategorizedTransaction categorizedTransaction(Transaction transaction);

    boolean supports (Transaction transaction);
}
