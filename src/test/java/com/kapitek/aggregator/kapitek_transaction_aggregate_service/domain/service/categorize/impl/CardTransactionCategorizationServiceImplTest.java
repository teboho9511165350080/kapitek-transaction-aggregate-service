package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.categorize.impl;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.MerchantCategory;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Source;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CardTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata.CardTransactionTestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CardTransactionCategorizationServiceImplTest {

    @InjectMocks
    private CardTransactionCategorizationServiceImpl service;

    @Test
    @DisplayName("supports should return true for CardTransaction")
    void supports_shouldReturnTrue_forCardTransaction() {
        CardTransaction transaction = CardTransactionTestData.getCardTransaction();
        assertTrue(service.supports(transaction));
    }

    @Test
    @DisplayName("supports should return false for non CardTransaction")
    void supports_shouldReturnFalse_forOtherTransactionType() {
        Transaction transaction = new Transaction();
        assertFalse(service.supports(transaction));
    }

    @Test
    @DisplayName("Should correctly categorize DEBIT card transaction")
    void shouldCategorizeDebitCardTransaction() {

        CardTransaction cardTransaction = CardTransactionTestData.getDebitCardTransaction(
                "200.00", "5.00", "5411", "Pick & Pay");

        CategorizedTransaction result = service.categorizedTransaction(cardTransaction);

        assertEquals(cardTransaction.getCustomerInfoFileKey(), result.getCustomerInfoFileKey());
        assertEquals(cardTransaction.getCardNumber(), result.getCardNumber());
        assertEquals(cardTransaction.getTransactionId(), result.getTransactionId());
        assertEquals(Source.CARD, result.getSource());
        assertEquals(Direction.DEBIT, result.getDirection());
        assertEquals(cardTransaction.getAmount(), result.getMoneyOut());
        assertEquals(BigDecimal.ZERO, result.getMoneyIn());
        assertEquals(cardTransaction.getFee(), result.getFee());
        assertEquals(cardTransaction.getDescription(), result.getDescription());
        assertNotNull(result.getCategory());
        assertEquals(MerchantCategory.FOOD.getName(), result.getCategory());
    }

    @Test
    @DisplayName("Should correctly categorize CREDIT card transaction")
    void shouldCategorizeCreditCardTransaction() {

        CardTransaction cardTransaction = CardTransactionTestData.getCreditCardTransaction(
       "150.00", "6011", "Refund");

        CategorizedTransaction result = service.categorizedTransaction(cardTransaction);

        assertEquals(Direction.CREDIT, result.getDirection());
        assertEquals(cardTransaction.getAmount(), result.getMoneyIn());
        assertEquals(BigDecimal.ZERO, result.getMoneyOut());
        assertEquals(Source.CARD, result.getSource());
        assertEquals(cardTransaction.getDescription(), result.getDescription());
        assertNotNull(result.getCategory());
    }

    @Test
    @DisplayName("Should return UNCATEGORIZED when MCC is not recognized")
    void shouldReturnUncategorized_whenMccIsUnknown() {

        CardTransaction cardTransaction = CardTransactionTestData.getDebitCardTransaction(
                "75.00", "0.00", "9999999", "Unknown Merchant");

        CategorizedTransaction result = service.categorizedTransaction(cardTransaction);

        assertNotNull(result.getCategory());
        assertFalse(result.getCategory().isBlank());
        assertEquals(CardTransactionCategorizationServiceImpl.UNCATEGORIZED, result.getCategory());
    }
}