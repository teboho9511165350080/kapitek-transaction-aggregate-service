package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.input.security.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class RealmAccess {

    private List<String> roles = new ArrayList<>();

    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}