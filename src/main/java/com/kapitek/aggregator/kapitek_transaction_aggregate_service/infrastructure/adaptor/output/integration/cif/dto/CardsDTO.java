package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardsDTO {
    private String creditCardNumber;
    private String prePaidCardNumber;
}
