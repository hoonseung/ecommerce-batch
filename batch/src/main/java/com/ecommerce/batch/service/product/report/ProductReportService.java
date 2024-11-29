package com.ecommerce.batch.service.product.report;

import com.ecommerce.batch.domain.product.report.repository.BrandReportRepository;
import com.ecommerce.batch.domain.product.report.repository.CategoryReportRepository;
import com.ecommerce.batch.domain.product.report.repository.ManufacturerReportRepository;
import com.ecommerce.batch.domain.product.report.repository.ProductStatusReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ProductReportService {

    private final ManufacturerReportRepository manufacturerReportRepository;
    private final ProductStatusReportRepository productStatusReportRepository;
    private final BrandReportRepository brandReportRepository;
    private final CategoryReportRepository categoryReportRepository;

    public Long getManufacturerReportCount(LocalDate date) {
        return manufacturerReportRepository.countByStatDate(date);
    }

    public Long getProductStatusReportCount(LocalDate date) {
        return productStatusReportRepository.countByStatDate(date);
    }

    public Long getBrandReportCount(LocalDate date) {
        return brandReportRepository.countByStatDate(date);
    }

    public Long getCategoryReportCount(LocalDate date) {
        return categoryReportRepository.countByStatDate(date);
    }
}
