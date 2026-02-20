package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.dto;

import lombok.Getter;

@Getter
public class AggregateRequestDTO {
    private String accountNumber;
    private String startDate;
    private String endDate;
}
