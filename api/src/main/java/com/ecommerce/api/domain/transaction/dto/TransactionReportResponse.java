package com.ecommerce.api.domain.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionReportResponse(
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

    public static TransactionReportResponse from(TransactionReportResult result) {
        return new TransactionReportResponse(
                result.transactionDate(),
                result.transactionType(),
                result.transactionCount(),
                result.totalAmount(),
                result.customerCount(),
                result.orderCount(),
                result.paymentMethodKindCount(),
                result.avgProductCount(),
                result.totalProductQuantity()
        );
    }
}
