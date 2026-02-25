package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.input.security.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class JwtMapperUtil {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static JwtClaims fromClaimsMap(Map<String, Object> claims) {
        return mapper.convertValue(claims, JwtClaims.class);
    }
}