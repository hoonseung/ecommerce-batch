package com.ecommerce.batch.domain.product.report.entity;

import com.ecommerce.batch.domain.product.report.entity.id.BrandReportId;
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
@IdClass(BrandReportId.class)
@Table(name = "brand_reports")
@Entity
public class BrandReport {

    @Id
    private LocalDate statDate;
    @Id
    private String brand;
    private Long productCount;
    private Double avgSalesPrice;
    private Integer maxSalesPrice;
    private Integer minSalesPrice;
    private Long totalStockQuantity;
    private Double avgStockQuantity;
    private Long potentialSalesAmount;


    public BrandReport(String brand, Long productCount, Double avgSalesPrice, Integer maxSalesPrice, Integer minSalesPrice, Long totalStockQuantity, Double avgStockQuantity, Long potentialSalesAmount) {
        this(LocalDate.now(),
                brand,
                productCount,
                avgSalesPrice,
                maxSalesPrice,
                minSalesPrice,
                totalStockQuantity,
                avgStockQuantity,
                potentialSalesAmount);
    }
}
