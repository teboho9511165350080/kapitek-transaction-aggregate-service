package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.service;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.mapper.AggregateResponseMapper;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.request.AggregateRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.AggregateResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.input.TransactionAggregationUseCase;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.TransactionSourcePort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.TransactionStoragePort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransactionSummary;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.Transaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.categorize.TransactionCategorizationService;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.summary.CategorizedTransactionSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionAggregationService implements TransactionAggregationUseCase {

    private final List<TransactionSourcePort<? extends Transaction>> transactionSourcePorts;

    private final List<TransactionCategorizationService> transactionCategorizationServices;

    private final CategorizedTransactionSummaryService categorizedTransactionSummaryService;

    private final TransactionStoragePort transactionStoragePort;

    @Override
    public AggregateResponse getRecentTransactionsSummary(String customerInfoFileKey, int months) {
        List<CategorizedTransaction> filterCategorizedTransactions = getCategorizedTransactions(
                customerInfoFileKey, LocalDate.now().minusMonths(months), LocalDate.now());

        CategorizedTransactionSummary summary = categorizedTransactionSummaryService
                .processCategorizedTransactionSummary(filterCategorizedTransactions);

        return AggregateResponseMapper.mapToAggregateResponse(filterCategorizedTransactions, summary);
    }

    @Override
    public AggregateResponse getRecentCategorizeAggregatedTransactions(String customerInfoFileKey, int months) {
        List<CategorizedTransaction> filterCategorizedTransactions = getCategorizedTransactions(
                customerInfoFileKey, LocalDate.now().minusMonths(months), LocalDate.now());

        return AggregateResponseMapper.mapToAggregateResponse(filterCategorizedTransactions);
    }

    @Override
    public AggregateResponse geTransactionsSummaryBetweenDates(AggregateRequest aggregateRequest) {
        List<CategorizedTransaction> filterCategorizedTransactions = getCategorizedTransactions(
                aggregateRequest.getCustomerInfoFileKey(),
                aggregateRequest.getStartDate(),
                aggregateRequest.getEndDate()
        );

        CategorizedTransactionSummary summary = categorizedTransactionSummaryService
                .processCategorizedTransactionSummary(filterCategorizedTransactions);

        return AggregateResponseMapper.mapToAggregateResponse(filterCategorizedTransactions, summary);
    }

    @Override
    public AggregateResponse geAggregatedCategorizedTransactionsBetweenDates(AggregateRequest aggregateRequest) {
        List<CategorizedTransaction> filterCategorizedTransactions = getCategorizedTransactions(
                aggregateRequest.getCustomerInfoFileKey(),
                aggregateRequest.getStartDate(),
                aggregateRequest.getEndDate()
        );

        return AggregateResponseMapper.mapToAggregateResponse(filterCategorizedTransactions);
    }

    private List<? extends Transaction> fetchAllTransactions(String customerInfoFileKey) {
        return transactionSourcePorts.stream()
                .flatMap(port -> port.fetchTransactions(customerInfoFileKey).stream())
                .toList();
    }

    private List<CategorizedTransaction> categorizeAllTransactions(List<? extends Transaction> transactions) {
        return transactions.stream()
                .map(this::categorizeTransaction)
                .toList();
    }

    private CategorizedTransaction categorizeTransaction(Transaction transaction) {
        return transactionCategorizationServices.stream()
                .filter(service -> service.supports(transaction))
                .findFirst()
                .map(service -> service.categorizedTransaction(transaction))
                .orElseThrow(() -> new RuntimeException("No categorizer found"));
    }

    private List<CategorizedTransaction> getCategorizedTransactions(String customerInfoFileKey, LocalDate start, LocalDate end) {
        List<? extends Transaction>  transactions = fetchAllTransactions(customerInfoFileKey);
        List<CategorizedTransaction> categorizedTransactions = categorizeAllTransactions(transactions);
        transactionStoragePort.saveAllTransactions(categorizedTransactions);

        List<CategorizedTransaction> filterCategorizedTransactions = transactionStoragePort.findBetweenDates(
                customerInfoFileKey, start, end
        );

        filterCategorizedTransactions.sort(
                Comparator.comparing(CategorizedTransaction::getTransactionDate).reversed()
        );
        return filterCategorizedTransactions;
    }
}
