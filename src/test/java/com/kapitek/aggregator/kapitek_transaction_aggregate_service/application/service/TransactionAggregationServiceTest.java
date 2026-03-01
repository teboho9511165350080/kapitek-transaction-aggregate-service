package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.service;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.request.AggregateRequest;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.AggregateResponse;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.TransactionSourcePort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.TransactionStoragePort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.AccountTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.categorize.TransactionCategorizationService;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.service.summary.CategorizedTransactionSummaryService;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata.AccountTransactionTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class TransactionAggregationServiceTest {

    @InjectMocks
    private TransactionAggregationService transactionAggregationService;

    @Mock
    private TransactionSourcePort<AccountTransaction> accountTransactionTransactionSourcePort;

    @Mock
    private TransactionCategorizationService accountTransactionCategorization;

    @Mock
    private CategorizedTransactionSummaryService categorizedTransactionSummaryService;

    @Mock
    private TransactionStoragePort transactionStoragePort;

    @BeforeEach
    void setup() {
        transactionAggregationService = new TransactionAggregationService(
                List.of(accountTransactionTransactionSourcePort),
                List.of(accountTransactionCategorization),
                categorizedTransactionSummaryService,
                transactionStoragePort
        );
    }

    @Test
    void getRecentTransactionsSummary_returnsAggregateResponseWithCategorizedTransactionsAndSummary() {

        AccountTransaction accountTransaction = AccountTransactionTestData.getAccountTransaction();

        commonMocks(accountTransaction);

        AggregateResponse aggregateResponse = transactionAggregationService.getRecentTransactionsSummary(
                "TEB 011", 1);

        Mockito.verify(accountTransactionTransactionSourcePort, Mockito.times(1))
                .fetchTransactions(Mockito.anyString());
        Mockito.verify(accountTransactionCategorization, Mockito.times(1))
                .categorizedTransaction(eq(accountTransaction));
        Mockito.verify(categorizedTransactionSummaryService, Mockito.times(1))
                .processCategorizedTransactionSummary(any());
        Mockito.verify(transactionStoragePort, Mockito.times(1))
                .saveAllTransactions(anyList());
    }

    @Test
    void geAggregatedCategorizedTransactionsBetweenDates_returnsAggregateResponseWithCategorizedTransactionsOny() {

        AccountTransaction accountTransaction = AccountTransactionTestData.getAccountTransaction();

        commonMocks(accountTransaction);

        AggregateResponse aggregateResponse = transactionAggregationService.geAggregatedCategorizedTransactionsBetweenDates(
                AggregateRequest.builder()
                        .customerInfoFileKey("customerInfoFileKey")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now())
                        .build());

        Mockito.verify(accountTransactionTransactionSourcePort, Mockito.times(1))
                .fetchTransactions(Mockito.anyString());
        Mockito.verify(accountTransactionCategorization, Mockito.times(1))
                .categorizedTransaction(eq(accountTransaction));
        Mockito.verify(categorizedTransactionSummaryService, Mockito.times(0))
                .processCategorizedTransactionSummary(any());
        Mockito.verify(transactionStoragePort, Mockito.times(1))
                .saveAllTransactions(anyList());
    }

    @Test
    void geTransactionsSummaryBetweenDates_returnsAggregateResponseWithCategorizedTransactionsOny() {

        AccountTransaction accountTransaction = AccountTransactionTestData.getAccountTransaction();

        commonMocks(accountTransaction);

        AggregateResponse aggregateResponse = transactionAggregationService.geTransactionsSummaryBetweenDates(
                AggregateRequest.builder()
                        .customerInfoFileKey("customerInfoFileKey")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now())
                        .build());

        Mockito.verify(accountTransactionTransactionSourcePort, Mockito.times(1))
                .fetchTransactions(Mockito.anyString());
        Mockito.verify(accountTransactionCategorization, Mockito.times(1))
                .categorizedTransaction(eq(accountTransaction));
        Mockito.verify(categorizedTransactionSummaryService, Mockito.times(1))
                .processCategorizedTransactionSummary(any());
        Mockito.verify(transactionStoragePort, Mockito.times(1))
                .saveAllTransactions(anyList());
    }

    @Test
    void getRecentCategorizeAggregatedTransactions_returnsAggregateResponseWithCategorizedTransactionsOnly() {

        AccountTransaction accountTransaction = AccountTransactionTestData.getAccountTransaction();

        commonMocks(accountTransaction);

        AggregateResponse aggregateResponse = transactionAggregationService.getRecentCategorizeAggregatedTransactions(
                "TEB 011", 1);

        Mockito.verify(accountTransactionTransactionSourcePort, Mockito.times(1))
                .fetchTransactions(Mockito.anyString());
        Mockito.verify(accountTransactionCategorization, Mockito.times(1))
                .categorizedTransaction(eq(accountTransaction));
        Mockito.verify(categorizedTransactionSummaryService, Mockito.times(0))
                .processCategorizedTransactionSummary(any());
        Mockito.verify(transactionStoragePort, Mockito.times(1))
                .saveAllTransactions(anyList());
    }

    private void commonMocks(AccountTransaction accountTransaction) {
        Mockito.when(accountTransactionTransactionSourcePort.fetchTransactions(Mockito.anyString()))
                .thenReturn(List.of(accountTransaction));
        Mockito.when(accountTransactionCategorization.supports(eq(accountTransaction)))
                .thenReturn(true);
        Mockito.when(accountTransactionCategorization.categorizedTransaction(eq(accountTransaction)))
                .thenReturn(CategorizedTransaction.builder().build());
    }
}