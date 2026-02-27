package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Category {
    SALARY ("Salary", Direction.CREDIT, List.of("payroll", "salary")),
    OTHER_INCOME ("Other Income", Direction.CREDIT, List.of("payment received", "payshap")),
    DIGITAL_PAYMENT("Digital Payment", Direction.DEBIT, List.of("payshap", "payment")),
    INCOMING_TRANSFER("Incoming Transfer", Direction.CREDIT, List.of("transfer")),
    OUTGOING_TRANSFER("Outgoing Transfer", Direction.DEBIT, List.of("transfer")),
    FEES("Fees", Direction.DEBIT, List.of("fees", "fee", "charge", "charges")),
    INVESTMENT("Investment", Direction.DEBIT, List.of("ee deposit")),
    DEPOSIT ("Cash Deposit", Direction.CREDIT, List.of("cash deposit")),
    WITHDRAWAL ("Cash Withdrawal", Direction.DEBIT, List.of("banking app cash sent")),
    INTEREST ("Interest", Direction.CREDIT, List.of("interest received"));

    private final String name;
    private final Direction direction;
    private final List<String> keywords;

    public static Category fromDescription(String description, Direction direction) {
        if (description == null || description.isEmpty()) {
            return null;
        }

        String descLower = description.toLowerCase();

        return Arrays.stream(Category.values())
                .filter(category -> category.getDirection().equals(direction))
                .filter(category -> category.getKeywords().stream()
                        .anyMatch(keyword -> descLower.contains(keyword.toLowerCase()))
                )
                .findFirst()
                .orElse(null);
    }
}
