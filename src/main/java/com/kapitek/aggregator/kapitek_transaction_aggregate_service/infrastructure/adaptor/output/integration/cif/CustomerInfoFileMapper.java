package com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.Accounts;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.Cards;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.CustomerInfoFile;
import com.kapitek.aggregator.kapitek_transaction_aggregate_service.infrastructure.adaptor.output.integration.cif.dto.CustomerInfoFileResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerInfoFileMapper {

    public static CustomerInfoFile mapCustomerInfoFile(CustomerInfoFileResponseDTO customerInfoFileResponseDTO) {

        Accounts accounts = Accounts.builder()
                .chequeAccountNumber(customerInfoFileResponseDTO.getAccounts().getChequeAccountNumber())
                .savingsAccountNumber(customerInfoFileResponseDTO.getAccounts().getSavingsAccountNumber())
                .build();

        Cards cards = Cards.builder()
                .prePaidCardNumber(customerInfoFileResponseDTO.getCards().getPrePaidCardNumber())
                .creditCardNumber(customerInfoFileResponseDTO.getCards().getCreditCardNumber())
                .build();

        return CustomerInfoFile.builder()
                .customerInfoFileKey(customerInfoFileResponseDTO.getCustomerInfoFileKey())
                .accounts(accounts)
                .cards(cards)
                .build();
    }
}
