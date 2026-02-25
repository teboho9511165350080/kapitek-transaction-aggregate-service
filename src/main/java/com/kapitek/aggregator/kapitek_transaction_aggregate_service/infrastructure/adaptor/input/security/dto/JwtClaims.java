package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.input.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtClaims {
    @JsonProperty("realm_access")
    private RealmAccess realmAccess;
}