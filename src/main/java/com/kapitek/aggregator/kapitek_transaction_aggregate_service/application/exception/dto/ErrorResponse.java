package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.exeption.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    private Instant timestamp;
}