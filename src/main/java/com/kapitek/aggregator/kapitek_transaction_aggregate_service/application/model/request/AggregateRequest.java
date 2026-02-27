package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(
        name = "AggregateRequest",
        description = "Request body for aggregating & categorizing transactions within a given date range"
)
public class AggregateRequest {

    @NotNull
    @Schema(
            description = "Unique key referencing the stored customer information file",
            example = "6cc698972881-46d4-85f9-f5186ae260e7",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String customerInfoFileKey;

    @NotNull
    @Schema(
            description = "Start date for transaction aggregation",
            example = "2025-01-01",
            type = "string",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate startDate;

    @NotNull
    @Schema(
            description = "End date for transaction aggregation",
            example = "2025-01-31",
            type = "string",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate endDate;
}