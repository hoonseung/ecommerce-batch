package com.ecommerce.api.domain.product.report.repository;

import com.ecommerce.api.domain.product.report.entity.BrandReport;
import com.ecommerce.api.domain.product.report.entity.id.BrandReportId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrandReportRepository extends JpaRepository<BrandReport, BrandReportId> {

    List<BrandReport> findAllByStatDate(LocalDate date);
}
