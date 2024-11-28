package com.ecommerce.api.domain.product.report.dto.response;

import com.ecommerce.api.domain.product.report.dto.result.ProductReportResults;

import java.util.List;

public record ProductReportResponse(

        List<BrandReportResponse> brandReports,
        List<CategoryReportResponse> categoryReports,
        List<ManufacturerReportResponse> manufacturerReports,
        List<ProductStatusReportResponse> productStatusReports
) {

    public static ProductReportResponse from(ProductReportResults productReportResults) {
        return new ProductReportResponse(
                productReportResults.brandReports().stream().map(BrandReportResponse::from).toList(),
                productReportResults.categoryReports().stream().map(CategoryReportResponse::from).toList(),
                productReportResults.manufacturerReports().stream().map(ManufacturerReportResponse::from).toList(),
                productReportResults.productStatusReports().stream().map(ProductStatusReportResponse::from).toList()
        );
    }
}
