package com.kapitek.aggregator.kapitek_transaction_aggregate_service.testdata;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.enums.Direction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.domain.model.AccountTransaction;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.account.dto.AccountTransactionDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountTransactionTestData {

    public static AccountTransaction getAccountTransaction() {
        return AccountTransaction.builder()
                .customerInfoFileKey("TEB 0013")
                .transactionId("transactionId")
                .accountNumber("1130992")
                .transactionDate(LocalDateTime.now())
                .build();
    }

    public static AccountTransactionDTO getAccountTransactionDTO() {
        return AccountTransactionDTO.builder()
                .referenceId("referenceId")
                .accountNbr("1130992")
                .txDate(LocalDateTime.of(2024, 1, 1, 0,0,0))
                .build();
    }

    public static AccountTransaction getAccountTransaction(String amount, String fee, String description) {
        AccountTransaction accountTransaction = getAccountTransaction();

        accountTransaction.setAmount(new BigDecimal(amount));
        accountTransaction.setFee(new BigDecimal(fee));
        accountTransaction.setDescription(description);

        return accountTransaction;
    }

    public static AccountTransactionDTO getAccountTransactionDTO(String amount, String fee, String description) {
        AccountTransactionDTO accountTransactionDTO = getAccountTransactionDTO();

        accountTransactionDTO.setTxAmount(amount);
        accountTransactionDTO.setTxCharge(fee);
        accountTransactionDTO.setReference(description);

        return accountTransactionDTO;
    }

    public static AccountTransaction getCreditAccountTransaction(String amount, String description) {
        AccountTransaction accountTransaction = getAccountTransaction(amount, "0.00", description);

        accountTransaction.setDirection(Direction.CREDIT);

        return accountTransaction;
    }

    public static AccountTransactionDTO getCreditAccountTransactionDTO(String amount, String description) {
        AccountTransactionDTO accountTransactionDTO = getAccountTransactionDTO(amount, "0.00", description);

        accountTransactionDTO.setDirection(Direction.CREDIT.name());

        return accountTransactionDTO;
    }

    public static AccountTransaction getDebitAccountTransaction(String amount, String fee, String description) {
        AccountTransaction accountTransaction = getAccountTransaction(amount, fee, description);

        accountTransaction.setDirection(Direction.DEBIT);

        return accountTransaction;
    }

    public static AccountTransactionDTO getDebitAccountTransactionDTO(String amount, String fee, String description) {
        AccountTransactionDTO accountTransactionDTO = getAccountTransactionDTO(amount, fee, description);

        accountTransactionDTO.setDirection(Direction.DEBIT.name());

        return accountTransactionDTO;
    }
}
