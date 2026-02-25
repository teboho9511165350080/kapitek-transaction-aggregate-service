package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AccountTransaction extends Transaction {
    private String accountNumber;
}
