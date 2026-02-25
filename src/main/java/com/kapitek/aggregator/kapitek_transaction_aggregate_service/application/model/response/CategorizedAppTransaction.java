package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CategorizedAppTransaction {
    private LocalDate date;
    private String description;
    private String category;
    private String moneyIn;
    private String moneyOut;
    private BigDecimal fee;
}
