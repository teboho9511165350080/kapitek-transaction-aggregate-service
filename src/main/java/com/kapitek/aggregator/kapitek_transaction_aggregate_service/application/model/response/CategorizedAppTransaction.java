package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@Schema(
        name = "CategorizedAppTransaction",
        description = "Represents a categorized transaction with inflow, outflow, and associated fee information."
)
public class CategorizedAppTransaction {

    @Schema(
            description = "Transaction date",
            example = "2025-02-15",
            type = "string",
            format = "date"
    )
    private LocalDate date;

    @Schema(
            description = "Transaction description",
            example = "Payshap Payment Received: M. Mbele (079 123 1230)"
    )
    private String description;

    @Schema(
            description = "Category assigned to the transaction",
            example = "Other Income"
    )
    private String category;

    @Schema(
            description = "Amount credited to the account",
            example = "1500.00",
            nullable = true
    )
    private String moneyIn;

    @Schema(
            description = "Amount debited from the account",
            example = "250.00",
            nullable = true
    )
    private String moneyOut;

    @Schema(
            description = "Transaction processing fee",
            example = "2.50",
            type = "number",
            format = "decimal"
    )
    private BigDecimal fee;
}