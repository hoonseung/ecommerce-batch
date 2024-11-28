package com.ecommerce.api.domain.product.report.repository;

import com.ecommerce.api.domain.product.report.entity.ManufacturerReport;
import com.ecommerce.api.domain.product.report.entity.id.ManufacturerReportId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ManufacturerReportRepository extends JpaRepository<ManufacturerReport, ManufacturerReportId> {

    List<ManufacturerReport> findAllByStatDate(LocalDate date);
}