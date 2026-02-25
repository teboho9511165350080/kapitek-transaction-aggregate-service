package com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.port.output;

import com.kapitek.aggregator.kapitek_transaction_aggregate_service.application.model.response.CustomerInfoFile;

public interface CustomerInfoPort {
    CustomerInfoFile getFileLinkedToCustomerInfoKey(String customerInfoFileKey);
}
