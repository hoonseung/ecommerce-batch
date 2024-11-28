package com.ecommerce.api.domain.product.report.dto.result;

import com.ecommerce.api.domain.product.report.entity.BrandReport;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BrandReportResult(

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

    public static BrandReportResult from(BrandReport brandReport) {
        return new BrandReportResult(
                brandReport.getStatDate(),
                brandReport.getBrand(),
                brandReport.getProductCount(),
                brandReport.getAvgSalesPrice(),
                brandReport.getMaxSalesPrice(),
                brandReport.getMinSalesPrice(),
                brandReport.getTotalStockQuantity(),
                brandReport.getAvgStockQuantity(),
                brandReport.getPotentialSalesAmount()
        );
    }
}
