package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
public class CategorizedTransactionSummary {

    private Map<String, BigDecimal> moneyInSummary;

    private Map<String, BigDecimal> moneyOutSummary;
}
