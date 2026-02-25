package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.calculation;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;

import java.math.BigDecimal;

public final class TransactionAmountCalculator {

    private TransactionAmountCalculator() {}

    public static BigDecimal moneyOut(Transaction transaction) {
        return Direction.DEBIT.equals(transaction.getDirection()) ?
                transaction.getAmount() :
                BigDecimal.ZERO;
    }

    public static BigDecimal moneyIn(Transaction transaction) {
        return Direction.CREDIT.equals(transaction.getDirection()) ?
                transaction.getAmount() :
                BigDecimal.ZERO;
    }
}