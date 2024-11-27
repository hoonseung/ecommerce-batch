package com.ecommerce.batch.service.monitoring;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class ProductReportService {

    private final JdbcTemplate jdbcTemplate;

    public Long getManufacturerReportCount(LocalDate date) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM manufacturer_reports WHERE stat_date = ?", Long.class, date);
    }

    public Long getProductStatusReportCount(LocalDate date) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM product_status_reports WHERE stat_date = ?", Long.class, date);
    }

    public Long getBrandReportCount(LocalDate date) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM brand_reports WHERE stat_date = ?", Long.class, date);
    }

    public Long getCategoryReportCount(LocalDate date) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM category_reports WHERE stat_date = ?", Long.class, date);
    }
}
