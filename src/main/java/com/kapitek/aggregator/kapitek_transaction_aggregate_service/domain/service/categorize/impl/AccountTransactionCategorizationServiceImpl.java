package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.categorize.impl;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.calculation.TransactionAmountCalculator;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Category;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Source;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.AccountTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.categorize.TransactionCategorizationService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("accountTransactionCategorization")
public class AccountTransactionCategorizationServiceImpl implements TransactionCategorizationService {

    @Override
    public CategorizedTransaction categorizedTransaction(Transaction transaction) {

        AccountTransaction accountTransaction = (AccountTransaction) transaction;

        return CategorizedTransaction.builder()
                .customerInfoFileKey(accountTransaction.getCustomerInfoFileKey())
                .accountNumber(accountTransaction.getAccountNumber())
                .transactionId(accountTransaction.getTransactionId())
                .transactionDate(accountTransaction.getTransactionDate())
                .source(Source.ACCOUNT)
                .direction(accountTransaction.getDirection())
                .category(getCategory(accountTransaction))
                .moneyIn(TransactionAmountCalculator.moneyIn(accountTransaction))
                .moneyOut(TransactionAmountCalculator.moneyOut(accountTransaction))
                .fee(accountTransaction.getFee())
                .description(accountTransaction.getDescription())
                .build();
    }

    @Override
    public boolean supports(Transaction transaction) {
        return transaction instanceof AccountTransaction;
    }

    private String getCategory(AccountTransaction transaction) {
        Category category = Category.fromDescription(transaction.getDescription(), transaction.getDirection());

        if (Objects.nonNull(category)) {
            return category.getName();
        }

        return UNCATEGORIZED;
    }
}
