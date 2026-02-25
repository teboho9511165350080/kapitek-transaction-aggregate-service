package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class AggregateResponse {
    List<CategorizedAppTransaction> transactions;
}
