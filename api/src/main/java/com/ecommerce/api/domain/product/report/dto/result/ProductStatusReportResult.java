package com.ecommerce.api.domain.product.report.dto.result;

import com.ecommerce.api.domain.product.report.entity.ProductStatusReport;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductStatusReportResult(

        LocalDate statDate,
        String productStatus,
        Long productCount,
        BigDecimal avgStockQuantity
) {

    public static ProductStatusReportResult from(ProductStatusReport productStatusReport) {
        return new ProductStatusReportResult(
                productStatusReport.getStatDate(),
                productStatusReport.getProductStatus(),
                productStatusReport.getProductCount(),
                productStatusReport.getAvgStockQuantity()
        );
    }
}
