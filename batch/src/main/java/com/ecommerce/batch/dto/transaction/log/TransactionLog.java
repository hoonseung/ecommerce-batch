package com.ecommerce.batch.dto.transaction.log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionLog(
        String timestamp,
        String level,
        String thread,
        TransactionMdc mdc,
        String logger,
        String message
) {

    public String transactionType() {
        return mdc.transactionType();
    }

    public String transactionStatus() {
        return mdc.transactionStatus();
    }

    public String getTotalAmount() {
        return mdc.getTotalAmount();
    }

    public String getTotalProductQuantity() {
        return mdc.getTotalProductQuantity();
    }

    public String getOrderId() {
        return mdc.getOrderId();
    }

    public String getCustomerId() {
        return mdc.getCustomerId();
    }

    public String getPaymentMethod() {
        return mdc.getPaymentMethod();
    }

    public String getProductKindCount() {
        return mdc.getProductKindCount();
    }
}
