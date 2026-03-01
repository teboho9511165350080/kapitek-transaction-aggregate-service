package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CardTransaction extends Transaction {
    private String mcc;
    private String cardNumber;
}
