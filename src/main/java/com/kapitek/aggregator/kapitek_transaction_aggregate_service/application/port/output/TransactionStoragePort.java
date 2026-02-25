package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionStoragePort {

    void saveTransaction(CategorizedTransaction categorizedTransaction);

    void saveAllTransactions (List<CategorizedTransaction> categorizedTransactions);

    List<CategorizedTransaction> findBetweenDates(String customerInfoFileKey, LocalDate start, LocalDate end);
}
