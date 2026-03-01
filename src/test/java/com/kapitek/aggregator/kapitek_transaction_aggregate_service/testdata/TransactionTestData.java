package com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionTestData {

    public static Transaction getCreditTransaction() {
        return Transaction.builder()
                .amount(new BigDecimal("300.25"))
                .direction(Direction.CREDIT)
                .build();
    }

    public static Transaction getDebitTransaction() {
        return Transaction.builder()
                .amount(new BigDecimal("300.25"))
                .direction(Direction.DEBIT)
                .build();
    }
}
