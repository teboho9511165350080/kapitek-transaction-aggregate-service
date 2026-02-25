package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.persistence;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Source;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorizedTransactionEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String transactionId;

    private String accountNumber;

    private String cardNumber;

    private String customerInfoFileKey;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Source source;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    @Column(nullable = false)
    private String category;

    @Column(precision = 19, scale = 2)
    private BigDecimal moneyIn;

    @Column(precision = 19, scale = 2)
    private BigDecimal moneyOut;

    @Column(precision = 19, scale = 2)
    private BigDecimal fee;

    private String description;
}
