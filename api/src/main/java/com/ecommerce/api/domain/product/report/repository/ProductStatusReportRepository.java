package com.ecommerce.api.domain.product.report.repository;

import com.ecommerce.api.domain.product.report.entity.ProductStatusReport;
import com.ecommerce.api.domain.product.report.entity.id.ProductStatusReportId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductStatusReportRepository extends JpaRepository<ProductStatusReport, ProductStatusReportId> {

    List<ProductStatusReport> findAllByStatDate(LocalDate date);
}
