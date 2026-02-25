package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Accounts {
    private String chequeAccountNumber;
    private String savingsAccountNumber;
}