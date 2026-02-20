package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.TransactionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    private String transactionId;
    private String accountNumber;
    private BigDecimal amount;
    private LocalDateTime bookingDate;
    private TransactionType transactionType;
    private Direction direction;
}
