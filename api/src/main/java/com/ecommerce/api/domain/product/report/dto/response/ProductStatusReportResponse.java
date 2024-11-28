package com.ecommerce.api.domain.product.report.dto.response;

import com.ecommerce.api.domain.product.report.dto.result.ProductStatusReportResult;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductStatusReportResponse(

        LocalDate statDate,
        String productStatus,
        Long productCount,
        BigDecimal avgStockQuantity
) {

    public static ProductStatusReportResponse from(ProductStatusReportResult productStatusReportResult) {
        return new ProductStatusReportResponse(
                productStatusReportResult.statDate(),
                productStatusReportResult.productStatus(),
                productStatusReportResult.productCount(),
                productStatusReportResult.avgStockQuantity()
        );
    }
}
