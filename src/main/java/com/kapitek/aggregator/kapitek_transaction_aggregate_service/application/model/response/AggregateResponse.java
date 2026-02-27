package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransactionSummary;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@Schema(
        name = "AggregateResponse",
        description = "Wrapper response containing categorized transactions"
)
public class AggregateResponse {

    @ArraySchema(
            schema = @Schema(implementation = CategorizedAppTransaction.class),
            arraySchema = @Schema(description = "List of categorized transactions")
    )
    private List<CategorizedAppTransaction> transactions;

    @ArraySchema(
            schema = @Schema(implementation = CategorizedTransactionSummary.class),
            arraySchema = @Schema(description = "provides income and expense amount per category")
    )
    private CategorizedTransactionSummary summary;
}