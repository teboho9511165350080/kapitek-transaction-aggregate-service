package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.exeption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DownstreamServiceException extends RuntimeException {

    private final int status;

    private final String body;
}
