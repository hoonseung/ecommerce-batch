package com.ecommerce.api.domain.product.report.dto.response;

import com.ecommerce.api.domain.product.report.dto.result.ManufacturerReportResult;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManufacturerReportResponse(

        LocalDate statDate,
        String manufacturer,
        Long productCount,
        BigDecimal avgSalesPrice,
        BigDecimal potentialSalesAmount
) {

    public static ManufacturerReportResponse from(ManufacturerReportResult manufacturerReportResult) {
        return new ManufacturerReportResponse(
                manufacturerReportResult.statDate(),
                manufacturerReportResult.manufacturer(),
                manufacturerReportResult.productCount(),
                manufacturerReportResult.avgSalesPrice(),
                manufacturerReportResult.potentialSalesAmount()
        );
    }
}
