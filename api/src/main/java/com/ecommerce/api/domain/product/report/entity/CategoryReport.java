package com.ecommerce.api.domain.product.report.entity;

import com.ecommerce.api.domain.product.report.entity.id.CategoryReportId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
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
    private BigDecimal avgSalesPrice;
    private BigDecimal maxSalesPrice;
    private BigDecimal minSalesPrice;
    private Integer totalStockQuantity;
    private BigDecimal potentialSalesAmount;


}
