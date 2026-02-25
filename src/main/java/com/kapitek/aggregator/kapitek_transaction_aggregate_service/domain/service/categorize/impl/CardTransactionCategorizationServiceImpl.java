package com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.categorize.impl;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.calculation.TransactionAmountCalculator;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.MerchantCategoryCodes;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Source;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CardTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.categorize.TransactionCategorizationService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("cardTransactionCategorization")
public class CardTransactionCategorizationServiceImpl implements TransactionCategorizationService {
    @Override
    public CategorizedTransaction categorizedTransaction(Transaction transaction) {

        CardTransaction cardTransaction = (CardTransaction) transaction;

        return CategorizedTransaction.builder()
                .customerInfoFileKey(cardTransaction.getCustomerInfoFileKey())
                .cardNumber(cardTransaction.getCardNumber())
                .transactionId(cardTransaction.getTransactionId())
                .transactionDate(cardTransaction.getTransactionDate())
                .source(Source.CARD)
                .direction(cardTransaction.getDirection())
                .category(getCategory(cardTransaction))
                .moneyIn(TransactionAmountCalculator.moneyIn(cardTransaction))
                .moneyOut(TransactionAmountCalculator.moneyOut(cardTransaction))
                .fee(cardTransaction.getFee())
                .description(cardTransaction.getDescription())
                .build();
    }

    @Override
    public boolean supports(Transaction transaction) {
        return transaction instanceof CardTransaction;
    }

    private String getCategory(CardTransaction transaction) {
        MerchantCategoryCodes merchantCategoryCodes = MerchantCategoryCodes.getMerchantCategoryCode(transaction.getMcc());

        if (Objects.nonNull(merchantCategoryCodes)) {
            return merchantCategoryCodes.getMerchantCategory().getName();
        }

        return UNCATEGORIZED;
    }
}
