package com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategorizedTransactionTestData {

    public static CategorizedTransaction getCreditTransaction(String category, String moneyIn) {
        return CategorizedTransaction.builder()
                .moneyIn(new BigDecimal(moneyIn))
                .direction(Direction.CREDIT)
                .category(category)
                .build();
    }

    public static CategorizedTransaction getDebitTransaction(String category, String moneyOut, String fee) {
        return CategorizedTransaction.builder()
                .moneyOut(new BigDecimal(moneyOut))
                .fee(new BigDecimal(fee))
                .direction(Direction.DEBIT)
                .category(category)
                .build();
    }
}
