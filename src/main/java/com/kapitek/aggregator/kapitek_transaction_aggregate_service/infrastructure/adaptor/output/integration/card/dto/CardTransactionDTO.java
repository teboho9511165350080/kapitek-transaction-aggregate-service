package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.card.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardTransactionDTO {
    private String retrievalReferenceNumber;
    private String cardNumber;
    private String direction;
    private String merchantName;
    private String merchantCategoryCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;
    private String amount;
    private String paymentFee;
    private String reference;
}
