package com.ecommerce.batch.dto.transaction.log;

public record TransactionMdc(
        String transactionType,
        String totalAmount,
        String orderId,
        String transactionStatus,
        String totalProductQuantity,
        String customerId,
        String paymentMethod,
        String productKindCount
) {

    public String getTotalAmount() {
        if ("N/A".equals(totalAmount)) {
            return "0";
        }
        return totalAmount;
    }


    public String getTotalProductQuantity() {
        if ("N/A".equals(totalProductQuantity)) {
            return "0";
        }
        return totalProductQuantity;
    }

    public String getOrderId() {
        if ("N/A".equals(orderId)) {
            return "0";
        }
        return orderId;
    }

    public String getCustomerId() {
        if ("N/A".equals(customerId)) {
            return "0";
        }
        return customerId;
    }

    public String getPaymentMethod() {
        if ("N/A".equals(paymentMethod)) {
            return "NONE";
        }
        return paymentMethod;
    }

    public String getProductKindCount() {
        if ("N/A".equals(productKindCount)) {
            return "0";
        }
        return productKindCount;
    }
}
