package com.ecommerce.batch.domain.product.report.entity;

import com.ecommerce.batch.domain.product.report.entity.id.ManufacturerReportId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Setter
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
    private Double avgSalesPrice;
    private Long potentialSalesAmount;


    public ManufacturerReport(String manufacturer, Long productCount, Double avgSalesPrice, Long potentialSalesAmount) {
        this(LocalDate.now(),
                manufacturer,
                productCount,
                avgSalesPrice,
                potentialSalesAmount);
    }
}
