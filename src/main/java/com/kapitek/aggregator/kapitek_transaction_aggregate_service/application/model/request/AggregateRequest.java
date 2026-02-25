package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AggregateRequest {
    private String customerInfoFileKey;
    private LocalDate startDate;
    private LocalDate endDate;
}
