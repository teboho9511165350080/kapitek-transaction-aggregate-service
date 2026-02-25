package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.TransactionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Transaction {
    private String customerInfoFileKey;
    private String transactionId;
    private BigDecimal amount;
    private BigDecimal fee;
    private LocalDateTime transactionDate;
    private TransactionType transactionType;
    private Direction direction;
    private String description;
}
