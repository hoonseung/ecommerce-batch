package com.ecommerce.api.domain.product.report.service;

import com.ecommerce.api.domain.product.report.dto.result.ProductReportResults;
import com.ecommerce.api.domain.product.report.repository.BrandReportRepository;
import com.ecommerce.api.domain.product.report.repository.CategoryReportRepository;
import com.ecommerce.api.domain.product.report.repository.ManufacturerReportRepository;
import com.ecommerce.api.domain.product.report.repository.ProductStatusReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductReportService {

    private final BrandReportRepository brandReportRepository;
    private final CategoryReportRepository categoryReportRepository;
    private final ManufacturerReportRepository manufacturerReportRepository;
    private final ProductStatusReportRepository productStatusReportRepository;

    public ProductReportResults findReports(LocalDate date) {
        return ProductReportResults.of(
                brandReportRepository.findAllByStatDate(date),
                categoryReportRepository.findAllByStatDate(date),
                manufacturerReportRepository.findAllByStatDate(date),
                productStatusReportRepository.findAllByStatDate(date)
        );
    }
}
