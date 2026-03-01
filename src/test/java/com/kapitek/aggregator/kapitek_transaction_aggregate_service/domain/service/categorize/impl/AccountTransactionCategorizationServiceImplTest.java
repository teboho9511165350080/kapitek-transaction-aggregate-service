package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.categorize.impl;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Category;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Source;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.AccountTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata.AccountTransactionTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class AccountTransactionCategorizationServiceImplTest {

    @InjectMocks
    private AccountTransactionCategorizationServiceImpl service;

    @Test
    @DisplayName("supports should return true for AccountTransaction")
    void supports_shouldReturnTrue_forAccountTransaction() {
        AccountTransaction transaction = AccountTransactionTestData.getAccountTransaction();
        Assertions.assertTrue(service.supports(transaction));
    }

    @Test
    @DisplayName("supports should return false for non AccountTransaction")
    void supports_shouldReturnFalse_forOtherTransactionType() {
        Transaction transaction = new Transaction();
        Assertions.assertFalse(service.supports(transaction));
    }

    @Test
    @DisplayName("Should correctly map DEBIT account transaction")
    void shouldCategorizeDebitTransactionCorrectly() {

        AccountTransaction accountTransaction = AccountTransactionTestData.getDebitAccountTransaction(
                "250.00", "10.00", "ATM Withdrawal");

        CategorizedTransaction result = service.categorizedTransaction(accountTransaction);

        Assertions.assertEquals(accountTransaction.getCustomerInfoFileKey(), result.getCustomerInfoFileKey());
        Assertions.assertEquals(accountTransaction.getAccountNumber(), result.getAccountNumber());
        Assertions.assertEquals(accountTransaction.getTransactionId(), result.getTransactionId());
        Assertions.assertEquals(Source.ACCOUNT, result.getSource());
        Assertions.assertEquals(Direction.DEBIT, result.getDirection());
        Assertions.assertEquals(accountTransaction.getAmount(), result.getMoneyOut());
        Assertions.assertEquals(BigDecimal.ZERO, result.getMoneyIn());
        Assertions.assertEquals(accountTransaction.getFee(), result.getFee());
        Assertions.assertEquals(accountTransaction.getDescription(), result.getDescription());
        Assertions.assertNotNull(result.getCategory());
        Assertions.assertEquals(Category.WITHDRAWAL.getName(), result.getCategory());
    }

    @Test
    @DisplayName("Should correctly map CREDIT account transaction")
    void shouldCategorizeCreditTransactionCorrectly() {

        AccountTransaction accountTransaction = AccountTransactionTestData.getCreditAccountTransaction(
                "1000.00", "Kapitek payroll");

        CategorizedTransaction result =
                service.categorizedTransaction(accountTransaction);

        Assertions.assertEquals(Direction.CREDIT, result.getDirection());
        Assertions.assertEquals(accountTransaction.getAmount(), result.getMoneyIn());
        Assertions.assertEquals(BigDecimal.ZERO, result.getMoneyOut());
        Assertions.assertEquals(Source.ACCOUNT, result.getSource());
        Assertions.assertEquals(accountTransaction.getDescription(), result.getDescription());
        Assertions.assertNotNull(result.getCategory());
        Assertions.assertEquals(Category.SALARY.getName(), result.getCategory());
    }

    @Test
    @DisplayName("Should return UNCATEGORIZED when Category.fromDescription returns null")
    void shouldReturnUncategorized_whenCategoryIsNull() {

        AccountTransaction accountTransaction = AccountTransactionTestData.getDebitAccountTransaction(
                "50.00", "0.00", "This description will definitely not be categorized");

        CategorizedTransaction result = service.categorizedTransaction(accountTransaction);

        Assertions.assertNotNull(result.getCategory());
        Assertions.assertFalse(result.getCategory().isBlank());
        Assertions.assertEquals(AccountTransactionCategorizationServiceImpl.UNCATEGORIZED, result.getCategory());
    }
}