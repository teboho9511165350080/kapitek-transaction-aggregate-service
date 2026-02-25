package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<CategorizedTransactionEntity, Long> {

    List<CategorizedTransactionEntity> findByCustomerInfoFileKeyAndTransactionDateBetween(
            String accountNumber,
            LocalDateTime start,
            LocalDateTime end
    );
}
