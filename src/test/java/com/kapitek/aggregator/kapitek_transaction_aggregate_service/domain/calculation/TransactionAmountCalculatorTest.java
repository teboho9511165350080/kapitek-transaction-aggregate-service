package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.calculation;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata.TransactionTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class TransactionAmountCalculatorTest {

    @Test
    @DisplayName("moneyOut should return amount when direction is DEBIT")
    void moneyOut_shouldReturnAmount_whenDirectionIsDebit() {
        Transaction transaction = TransactionTestData.getDebitTransaction();

        BigDecimal result = TransactionAmountCalculator.moneyOut(transaction);

        Assertions.assertEquals(transaction.getAmount(), result);
    }

    @Test
    @DisplayName("moneyOut should return ZERO when direction is CREDIT")
    void moneyOut_shouldReturnZero_whenDirectionIsCredit() {
        Transaction transaction = TransactionTestData.getCreditTransaction();

        BigDecimal result = TransactionAmountCalculator.moneyOut(transaction);

        Assertions.assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("moneyIn should return amount when direction is CREDIT")
    void moneyIn_shouldReturnAmount_whenDirectionIsCredit() {
        Transaction transaction = TransactionTestData.getCreditTransaction();

        BigDecimal result = TransactionAmountCalculator.moneyIn(transaction);

        Assertions.assertEquals(transaction.getAmount(), result);
    }

    @Test
    @DisplayName("moneyIn should return ZERO when direction is DEBIT")
    void moneyIn_shouldReturnZero_whenDirectionIsDebit() {
        Transaction transaction = TransactionTestData.getDebitTransaction();

        BigDecimal result = TransactionAmountCalculator.moneyIn(transaction);

        Assertions.assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("moneyOut should return ZERO when direction is null")
    void moneyOut_shouldReturnZero_whenDirectionIsNull() {
        Transaction transaction = TransactionTestData.getDebitTransaction();
        transaction.setDirection(null);

        BigDecimal result = TransactionAmountCalculator.moneyOut(transaction);

        Assertions.assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("moneyIn should return ZERO when direction is null")
    void moneyIn_shouldReturnZero_whenDirectionIsNull() {
        Transaction transaction = TransactionTestData.getCreditTransaction();
        transaction.setDirection(null);

        BigDecimal result = TransactionAmountCalculator.moneyIn(transaction);

        Assertions.assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("moneyOut should throw NullPointerException when transaction is null")
    void moneyOut_shouldThrowException_whenTransactionIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                TransactionAmountCalculator.moneyOut(null));
    }

    @Test
    @DisplayName("moneyIn should throw NullPointerException when transaction is null")
    void moneyIn_shouldThrowException_whenTransactionIsNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                TransactionAmountCalculator.moneyIn(null));
    }
}