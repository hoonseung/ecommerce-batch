package com.ecommerce.api.domain.product.report.dto.response;

import com.ecommerce.api.domain.product.report.dto.result.BrandReportResult;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BrandReportResponse(

        LocalDate statDate,
        String brand,
        Long productCount,
        BigDecimal avgSalesPrice,
        BigDecimal maxSalesPrice,
        BigDecimal minSalesPrice,
        Integer totalStockQuantity,
        BigDecimal avgStockQuantity,
        BigDecimal potentialSalesAmount
) {

    public static BrandReportResponse from(BrandReportResult brandReportResult) {
        return new BrandReportResponse(
                brandReportResult.statDate(),
                brandReportResult.brand(),
                brandReportResult.productCount(),
                brandReportResult.avgSalesPrice(),
                brandReportResult.maxSalesPrice(),
                brandReportResult.minSalesPrice(),
                brandReportResult.totalStockQuantity(),
                brandReportResult.avgStockQuantity(),
                brandReportResult.potentialSalesAmount()
        );
    }
}
