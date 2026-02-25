package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.mapper;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.AggregateResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.CategorizedAppTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AggregateResponseMapper {

    public static AggregateResponse mapToAggregateResponse(List<CategorizedTransaction> transactions) {
        return AggregateResponse.builder()
                .transactions(mapToCategorizedTransactions(transactions))
                .build();
    }

    public static List<CategorizedAppTransaction> mapToCategorizedTransactions(List<CategorizedTransaction> transactions) {
        return transactions.stream()
                .map(AggregateResponseMapper::mapToCategorizedTransaction)
                .toList();
    }

    private static CategorizedAppTransaction mapToCategorizedTransaction(CategorizedTransaction categorizedTransaction) {
        return CategorizedAppTransaction.builder()
                .date(categorizedTransaction.getTransactionDate().toLocalDate())
                .description(categorizedTransaction.getDescription())
                .category(categorizedTransaction.getCategory())
                .moneyIn(String.valueOf(categorizedTransaction.getMoneyIn()))
                .moneyOut(String.valueOf(categorizedTransaction.getMoneyOut()))
                .fee(categorizedTransaction.getFee())
                .build();
    }
}
