package com.kapitek.aggregator.kapitek_transaction_aggregate_service.service.impl;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.AccountTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.service.TransactionCategorizationService;

@Service
public class AccountTransactionCategorizationServiceImpl implements TransactionCategorizationService<AccountTransaction> {

    @Override
    CategorizedTransaction categorizedTransaction(AccountTransaction transaction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
