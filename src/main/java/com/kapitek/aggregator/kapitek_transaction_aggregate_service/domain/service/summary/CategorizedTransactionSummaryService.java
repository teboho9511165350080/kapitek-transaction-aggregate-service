package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.summary;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransactionSummary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategorizedTransactionSummaryService {

    public CategorizedTransactionSummary processCategorizedTransactionSummary(List<CategorizedTransaction> categorizedTransactions) {
        Map<String, BigDecimal> moneyInSummary = new HashMap<>();
        Map<String, BigDecimal> moneyOutSummary = new HashMap<>();

        categorizedTransactions.forEach(categorizedTransaction -> {
            processMoneyInSummary(categorizedTransaction, moneyInSummary);
            processMoneyOutSummary(categorizedTransaction, moneyOutSummary);
        });


        return CategorizedTransactionSummary.builder()
                .moneyInSummary(moneyInSummary)
                .moneyOutSummary(moneyOutSummary)
                .build();
    }

    private void processMoneyOutSummary(CategorizedTransaction categorizedTransaction,
                                        Map<String, BigDecimal> moneyOutSummary) {

        if (Direction.DEBIT.equals(categorizedTransaction.getDirection())) {

            BigDecimal transactionTotalOut = BigDecimal.ZERO
                    .add(categorizedTransaction.getMoneyOut())
                    .add(categorizedTransaction.getFee());

            processMoneySummary(
                    moneyOutSummary,
                    categorizedTransaction.getCategory(),
                    transactionTotalOut
            );
        }
    }

    private void processMoneyInSummary(CategorizedTransaction categorizedTransaction,
                                       Map<String, BigDecimal> moneyInSummary) {

        if (Direction.CREDIT.equals(categorizedTransaction.getDirection())) {
            processMoneySummary(
                    moneyInSummary,
                    categorizedTransaction.getCategory(),
                    categorizedTransaction.getMoneyIn()
            );
        }
    }

    private static void processMoneySummary(Map<String, BigDecimal> moneySummary,
                                            String category,
                                            BigDecimal amount) {
        BigDecimal categoryTotal;

        if (moneySummary.containsKey(category)) {
            BigDecimal currentCategoryTotal = moneySummary.get(category);
            categoryTotal = currentCategoryTotal.add(amount);
        } else {
            categoryTotal = amount;
        }

        moneySummary.put(category, categoryTotal);
    }
}
