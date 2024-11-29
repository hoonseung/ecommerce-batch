package com.ecommerce.batch.domain.product.report.repository;


import com.ecommerce.batch.domain.product.report.entity.CategoryReport;
import com.ecommerce.batch.domain.product.report.entity.id.CategoryReportId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CategoryReportRepository extends JpaRepository<CategoryReport, CategoryReportId> {

    List<CategoryReport> findAllByStatDate(LocalDate date);

    Long countByStatDate(LocalDate date);
}
