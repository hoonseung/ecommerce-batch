package com.ecommerce.api.domain.product.report.entity;

import com.ecommerce.api.domain.product.report.entity.id.ManufacturerReportId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(ManufacturerReportId.class)
@Table(name = "manufacturer_reports")
@Entity
public class ManufacturerReport {
    @Id
    private LocalDate statDate;
    @Id
    private String manufacturer;
    private Long productCount;
    private BigDecimal avgSalesPrice;
    private BigDecimal potentialSalesAmount;

}
