package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerInfoFileResponseDTO {
    private String customerInfoFileKey;
    private String identityNumber;
    private AccountsDTO accounts;
    private CardsDTO cards;
}
