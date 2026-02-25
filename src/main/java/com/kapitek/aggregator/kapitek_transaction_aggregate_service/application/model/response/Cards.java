package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cards {
    private String creditCardNumber;
    private String prePaidCardNumber;
}
