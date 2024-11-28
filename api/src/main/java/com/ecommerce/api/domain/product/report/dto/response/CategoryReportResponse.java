package com.ecommerce.api.domain.product.report.dto.response;

import com.ecommerce.api.domain.product.report.dto.result.CategoryReportResult;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CategoryReportResponse(

        LocalDate statDate,
        String category,
        Long productCount,
        BigDecimal avgSalesPrice,
        BigDecimal maxSalesPrice,
        BigDecimal minSalesPrice,
        Integer totalStockQuantity,
        BigDecimal potentialSalesAmount
) {

    public static CategoryReportResponse from(CategoryReportResult categoryReportResult) {
        return new CategoryReportResponse(
                categoryReportResult.statDate(),
                categoryReportResult.category(),
                categoryReportResult.productCount(),
                categoryReportResult.avgSalesPrice(),
                categoryReportResult.maxSalesPrice(),
                categoryReportResult.minSalesPrice(),
                categoryReportResult.totalStockQuantity(),
                categoryReportResult.potentialSalesAmount()
        );
    }
}
