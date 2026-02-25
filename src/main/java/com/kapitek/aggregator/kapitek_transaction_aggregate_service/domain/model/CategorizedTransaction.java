package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Source;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CategorizedTransaction {

    private String customerInfoFileKey;

    private String cardNumber;

    private String accountNumber;

    private String transactionId;

    private LocalDateTime transactionDate;

    private Source source;

    private Direction direction;

    private String category;

    private BigDecimal moneyIn;

    private BigDecimal moneyOut;

    private BigDecimal fee;

    private String description;
}
