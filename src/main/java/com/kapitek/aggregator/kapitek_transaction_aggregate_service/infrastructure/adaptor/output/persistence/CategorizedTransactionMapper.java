package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.persistence;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategorizedTransactionMapper {

    public static CategorizedTransaction mapToCategorizedTransaction(CategorizedTransactionEntity categorizedTransactionEntity) {

        return CategorizedTransaction.builder()
                .customerInfoFileKey(categorizedTransactionEntity.getCustomerInfoFileKey())
                .cardNumber(categorizedTransactionEntity.getCardNumber())
                .accountNumber(categorizedTransactionEntity.getAccountNumber())
                .transactionId(categorizedTransactionEntity.getTransactionId())
                .transactionDate(categorizedTransactionEntity.getTransactionDate())
                .source(categorizedTransactionEntity.getSource())
                .direction(categorizedTransactionEntity.getDirection())
                .category(categorizedTransactionEntity.getCategory())
                .moneyIn(categorizedTransactionEntity.getMoneyIn())
                .moneyOut(categorizedTransactionEntity.getMoneyOut())
                .fee(categorizedTransactionEntity.getFee())
                .description(categorizedTransactionEntity.getDescription())
                .build();
    }

    public static CategorizedTransactionEntity mapToCategorizedTransactionEntity(CategorizedTransaction categorizedTransaction) {

        return CategorizedTransactionEntity.builder()
                .accountNumber(categorizedTransaction.getAccountNumber())
                .cardNumber(categorizedTransaction.getCardNumber())
                .customerInfoFileKey(categorizedTransaction.getCustomerInfoFileKey())
                .transactionId(categorizedTransaction.getTransactionId())
                .transactionDate(categorizedTransaction.getTransactionDate())
                .source(categorizedTransaction.getSource())
                .direction(categorizedTransaction.getDirection())
                .category(categorizedTransaction.getCategory())
                .moneyIn(categorizedTransaction.getMoneyIn())
                .moneyOut(categorizedTransaction.getMoneyOut())
                .fee(categorizedTransaction.getFee())
                .description(categorizedTransaction.getDescription())
                .build();
    }
}
