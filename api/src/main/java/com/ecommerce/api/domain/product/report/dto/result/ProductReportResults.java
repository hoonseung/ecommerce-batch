package com.ecommerce.api.domain.product.report.dto.result;

import com.ecommerce.api.domain.product.report.entity.BrandReport;
import com.ecommerce.api.domain.product.report.entity.CategoryReport;
import com.ecommerce.api.domain.product.report.entity.ManufacturerReport;
import com.ecommerce.api.domain.product.report.entity.ProductStatusReport;

import java.util.List;

public record ProductReportResults(
        List<com.ecommerce.api.domain.product.report.dto.result.BrandReportResult> brandReports,
        List<CategoryReportResult> categoryReports,
        List<com.ecommerce.api.domain.product.report.dto.result.ManufacturerReportResult> manufacturerReports,
        List<ProductStatusReportResult> productStatusReports
) {

    public static ProductReportResults of(List<BrandReport> brandReports,
                                          List<CategoryReport> categoryReports,
                                          List<ManufacturerReport> manufacturerReports,
                                          List<ProductStatusReport> productStatusReports) {
        return new ProductReportResults(
                brandReports.stream().map(com.ecommerce.api.domain.product.report.dto.result.BrandReportResult::from).toList(),
                categoryReports.stream().map(CategoryReportResult::from).toList(),
                manufacturerReports.stream().map(com.ecommerce.api.domain.product.report.dto.result.ManufacturerReportResult::from).toList(),
                productStatusReports.stream().map(ProductStatusReportResult::from).toList()
        );
    }
}
