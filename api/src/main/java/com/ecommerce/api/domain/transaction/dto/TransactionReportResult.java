package com.ecommerce.api.domain.transaction.dto;

import com.ecommerce.api.domain.transaction.entity.TransactionReport;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionReportResult(
        LocalDate transactionDate,
        String transactionType,
        Long transactionCount,
        Long totalAmount,
        Long customerCount,
        Long orderCount,
        Long paymentMethodKindCount,
        BigDecimal avgProductCount,
        Long totalProductQuantity
) {

    public static TransactionReportResult from(TransactionReport report) {
        return new TransactionReportResult(
                report.getTransactionDate(),
                report.getTransactionType(),
                report.getTransactionCount(),
                report.getTotalAmount(),
                report.getCustomerCount(),
                report.getOrderCount(),
                report.getPaymentMethodKindCount(),
                report.getAvgProductCount(),
                report.getTotalProductQuantity()
        );
    }
}
