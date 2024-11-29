package com.ecommerce.batch.domain.product.report.entity;

import com.ecommerce.batch.domain.product.report.entity.id.CategoryReportId;
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
@IdClass(CategoryReportId.class)
@Table(name = "category_reports")
@Entity
public class CategoryReport {
    @Id
    private LocalDate statDate;
    @Id
    private String category;
    private Long productCount;
    private Double avgSalesPrice;
    private Integer maxSalesPrice;
    private Integer minSalesPrice;
    private Long totalStockQuantity;
    private Long potentialSalesAmount;


    public CategoryReport(String category, Long productCount, Double avgSalesPrice, Integer maxSalesPrice, Integer minSalesPrice, Long totalStockQuantity, Long potentialSalesAmount) {
        this(LocalDate.now(),
                category,
                productCount,
                avgSalesPrice,
                maxSalesPrice,
                minSalesPrice,
                totalStockQuantity,
                potentialSalesAmount);
    }
}

