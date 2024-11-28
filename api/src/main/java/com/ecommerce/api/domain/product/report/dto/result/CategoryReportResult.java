package com.ecommerce.api.domain.product.report.dto.result;

import com.ecommerce.api.domain.product.report.entity.CategoryReport;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CategoryReportResult(

        LocalDate statDate,
        String category,
        Long productCount,
        BigDecimal avgSalesPrice,
        BigDecimal maxSalesPrice,
        BigDecimal minSalesPrice,
        Integer totalStockQuantity,
        BigDecimal potentialSalesAmount
) {

    public static CategoryReportResult from(CategoryReport categoryReport) {
        return new CategoryReportResult(
                categoryReport.getStatDate(),
                categoryReport.getCategory(),
                categoryReport.getProductCount(),
                categoryReport.getAvgSalesPrice(),
                categoryReport.getMaxSalesPrice(),
                categoryReport.getMinSalesPrice(),
                categoryReport.getTotalStockQuantity(),
                categoryReport.getPotentialSalesAmount()
        );
    }
}
