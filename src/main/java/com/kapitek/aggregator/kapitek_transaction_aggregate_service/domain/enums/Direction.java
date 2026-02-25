package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums;

public enum Direction {
    CREDIT,
    DEBIT;

    public static Direction fromValue(String value) {
        try {
            return Direction.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
