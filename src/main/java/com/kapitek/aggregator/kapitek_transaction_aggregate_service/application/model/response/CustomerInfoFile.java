package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerInfoFile {
    private String customerInfoFileKey;
    private String identityNumber;
    private Accounts accounts;
    private Cards cards;
}
