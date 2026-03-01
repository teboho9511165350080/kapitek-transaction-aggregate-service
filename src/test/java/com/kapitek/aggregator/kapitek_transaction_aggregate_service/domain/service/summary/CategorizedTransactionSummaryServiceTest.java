package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.summary;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransactionSummary;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata.CategorizedTransactionTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class CategorizedTransactionSummaryServiceTest {

    @InjectMocks
    private CategorizedTransactionSummaryService service;

    @Test
    @DisplayName("Should return empty summaries when input list is empty")
    void shouldReturnEmptySummary_whenNoTransactions() {
        CategorizedTransactionSummary result =
                service.processCategorizedTransactionSummary(List.of());

        Assertions.assertTrue(result.getMoneyInSummary().isEmpty());
        Assertions.assertTrue(result.getMoneyOutSummary().isEmpty());
    }

    @Test
    @DisplayName("Should aggregate CREDIT transactions into moneyInSummary")
    void shouldAggregateCreditTransactions() {
        CategorizedTransaction tx1 = CategorizedTransactionTestData.getCreditTransaction(
                "Salary", "1000.00");

        CategorizedTransaction tx2 = CategorizedTransactionTestData.getCreditTransaction(
                "Salary", "500.00");

        CategorizedTransactionSummary result = service.processCategorizedTransactionSummary(List.of(tx1, tx2));

        Map<String, BigDecimal> moneyIn = result.getMoneyInSummary();

        Assertions.assertEquals(new BigDecimal("1500.00"), moneyIn.get("Salary"));
        Assertions.assertTrue(result.getMoneyOutSummary().isEmpty());
    }

    @Test
    @DisplayName("Should aggregate DEBIT transactions into moneyOutSummary including fee")
    void shouldAggregateDebitTransactionsIncludingFee() {
        CategorizedTransaction tx1 = CategorizedTransactionTestData.getDebitTransaction(
                "Shopping", "200.00", "10.00");

        CategorizedTransaction tx2 = CategorizedTransactionTestData.getDebitTransaction(
                "Shopping", "100.00", "5.00");

        CategorizedTransactionSummary result = service.processCategorizedTransactionSummary(List.of(tx1, tx2));

        Map<String, BigDecimal> moneyOut = result.getMoneyOutSummary();

        // (200 + 10) + (100 + 5) = 315
        Assertions.assertEquals(new BigDecimal("315.00"), moneyOut.get("Shopping"));
        Assertions.assertTrue(result.getMoneyInSummary().isEmpty());
    }

    @Test
    @DisplayName("Should handle mixed CREDIT and DEBIT transactions")
    void shouldHandleMixedTransactions() {
        CategorizedTransaction credit = CategorizedTransactionTestData.getCreditTransaction(
                "Interest", "250.00");

        CategorizedTransaction debit = CategorizedTransactionTestData.getDebitTransaction(
                "Utilities", "120.00", "5.00");

        CategorizedTransactionSummary result = service.processCategorizedTransactionSummary(List.of(credit, debit));

        Assertions.assertEquals(new BigDecimal("250.00"), result.getMoneyInSummary().get("Interest"));

        Assertions.assertEquals(new BigDecimal("125.00"), result.getMoneyOutSummary().get("Utilities"));
    }

    @Test
    @DisplayName("Should aggregate multiple categories independently")
    void shouldAggregateMultipleCategoriesIndependently() {
        CategorizedTransaction salary = CategorizedTransactionTestData.getCreditTransaction(
                "Salary", "3000.00");

        CategorizedTransaction bonus = CategorizedTransactionTestData.getCreditTransaction(
                "Bonus", "1000.00");

        CategorizedTransactionSummary result = service.processCategorizedTransactionSummary(List.of(salary, bonus));

        Assertions.assertEquals(new BigDecimal("3000.00"), result.getMoneyInSummary().get("Salary"));

        Assertions.assertEquals(new BigDecimal("1000.00"), result.getMoneyInSummary().get("Bonus"));
    }
}