package com.ecommerce.api.domain.product.report.entity;

import com.ecommerce.api.domain.product.report.entity.id.ProductStatusReportId;
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
@IdClass(ProductStatusReportId.class)
@Table(name = "product_status_reports")
@Entity
public class ProductStatusReport {
    @Id
    private LocalDate statDate;
    @Id
    private String productStatus;
    private Long productCount;
    private BigDecimal avgStockQuantity;


}
