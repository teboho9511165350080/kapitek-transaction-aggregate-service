package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response;

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
}