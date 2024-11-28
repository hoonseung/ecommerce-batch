package com.ecommerce.api.domain.product.report.dto.result;

import com.ecommerce.api.domain.product.report.entity.ManufacturerReport;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManufacturerReportResult(

        LocalDate statDate,
        String manufacturer,
        Long productCount,
        BigDecimal avgSalesPrice,
        BigDecimal potentialSalesAmount
) {

    public static ManufacturerReportResult from(ManufacturerReport manufacturerReport) {
        return new ManufacturerReportResult(
                manufacturerReport.getStatDate(),
                manufacturerReport.getManufacturer(),
                manufacturerReport.getProductCount(),
                manufacturerReport.getAvgSalesPrice(),
                manufacturerReport.getPotentialSalesAmount()
        );
    }
}
