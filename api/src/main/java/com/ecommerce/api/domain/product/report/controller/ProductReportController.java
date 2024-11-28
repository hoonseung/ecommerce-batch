package com.ecommerce.api.domain.product.report.controller;

import com.ecommerce.api.domain.api.ApiResponse;
import com.ecommerce.api.domain.product.report.dto.response.ProductReportResponse;
import com.ecommerce.api.domain.product.report.service.ProductReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RequestMapping("/v1/product/reports")
@RequiredArgsConstructor
@RestController
public class ProductReportController {

    private final ProductReportService productReportService;

    @GetMapping("")
    public ApiResponse<ProductReportResponse> findReports(@RequestParam("dt") @DateTimeFormat(iso = DATE) LocalDate date) {
        ProductReportResponse reportResponse = ProductReportResponse.from(productReportService.findReports(date));
        return ApiResponse.success(reportResponse);
    }
}
