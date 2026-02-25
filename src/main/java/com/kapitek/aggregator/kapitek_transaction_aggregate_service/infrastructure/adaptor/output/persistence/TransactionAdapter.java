package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.persistence;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output.TransactionStoragePort;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.CategorizedTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class TransactionAdapter implements TransactionStoragePort {

    private final TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(CategorizedTransaction categorizedTransaction) {
        CategorizedTransactionEntity entity = CategorizedTransactionMapper.mapToCategorizedTransactionEntity(
                categorizedTransaction);

        transactionRepository.save(entity);
    }

    @Override
    public void saveAllTransactions(List<CategorizedTransaction> categorizedTransactions) {
        List<CategorizedTransactionEntity> entities = categorizedTransactions.stream()
                .map(CategorizedTransactionMapper::mapToCategorizedTransactionEntity)
                .toList();

        transactionRepository.saveAll(entities);
    }

    @Override
    public List<CategorizedTransaction> findBetweenDates(String customerInfoFileKey, LocalDate start, LocalDate end) {
        List<CategorizedTransactionEntity> entities = transactionRepository.findByCustomerInfoFileKeyAndTransactionDateBetween(
                customerInfoFileKey,
                start.atTime(LocalTime.MIN),
                end.atTime(LocalTime.MAX)
        );

        return entities.stream()
                .map(CategorizedTransactionMapper::mapToCategorizedTransaction)
                .collect(toList());
    }
}
