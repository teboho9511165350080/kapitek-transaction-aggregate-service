package com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CardTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.dto.CardTransactionDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardTransactionTestData {

    public static CardTransaction getCardTransaction() {
        return CardTransaction.builder()
                .customerInfoFileKey("TEB 0013")
                .cardNumber("1130992")
                .transactionId("transactionId")
                .transactionDate(LocalDateTime.now())
                .build();
    }

    public static CardTransactionDTO getCardTransactionDTO() {
        return CardTransactionDTO.builder()
                .cardNumber("1130992")
                .retrievalReferenceNumber("transactionId")
                .transactionDate(LocalDateTime.now())
                .merchantName("merchantName")
                .build();
    }

    public static CardTransaction getCardTransaction(String amount, String fee, String mcc, String description) {
        CardTransaction cardTransaction = getCardTransaction();

        cardTransaction.setAmount(new BigDecimal(amount));
        cardTransaction.setFee(new BigDecimal(fee));
        cardTransaction.setMcc(mcc);
        cardTransaction.setDescription(description);

        return cardTransaction;
    }

    public static CardTransactionDTO getCardTransactionDTO(String amount, String fee, String mcc, String description) {
        CardTransactionDTO cardTransaction = getCardTransactionDTO();

        cardTransaction.setAmount(amount);
        cardTransaction.setPaymentFee(fee);
        cardTransaction.setMerchantCategoryCode(mcc);
        cardTransaction.setReference(description);

        return cardTransaction;
    }

    public static CardTransaction getDebitCardTransaction(String amount, String fee, String mcc, String description) {
        CardTransaction accountTransaction = getCardTransaction(amount, fee, mcc, description);

        accountTransaction.setDirection(Direction.DEBIT);

        return accountTransaction;
    }

    public static CardTransactionDTO getDebitCardTransactionDTO(String amount, String fee, String mcc, String description) {
        CardTransactionDTO accountTransaction = getCardTransactionDTO(amount, fee, mcc, description);

        accountTransaction.setDirection(Direction.DEBIT.name());

        return accountTransaction;
    }

    public static CardTransaction getCreditCardTransaction(String amount, String mcc, String description) {
        CardTransaction accountTransaction = getCardTransaction(amount, "0.00", mcc, description);

        accountTransaction.setDirection(Direction.CREDIT);

        return accountTransaction;
    }

    public static CardTransactionDTO getCreditCardTransactionDTO(String amount, String mcc, String description) {
        CardTransactionDTO accountTransaction = getCardTransactionDTO(amount, "0.00", mcc, description);

        accountTransaction.setDirection(Direction.CREDIT.name());

        return accountTransaction;
    }
}
