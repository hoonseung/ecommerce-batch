package com.ecommerce.api.domain.product.report.repository;

import com.ecommerce.api.domain.product.report.entity.CategoryReport;
import com.ecommerce.api.domain.product.report.entity.id.CategoryReportId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CategoryReportRepository extends JpaRepository<CategoryReport, CategoryReportId> {

    List<CategoryReport> findAllByStatDate(LocalDate date);
}
