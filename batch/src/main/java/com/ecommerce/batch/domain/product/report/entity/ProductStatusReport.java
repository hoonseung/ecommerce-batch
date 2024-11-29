package com.ecommerce.batch.domain.product.report.entity;

import com.ecommerce.batch.domain.product.ProductStatus;
import com.ecommerce.batch.domain.product.report.entity.id.ProductStatusReportId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;

@Setter
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
    @Enumerated(STRING)
    private ProductStatus productStatus;
    private Long productCount;
    private Double avgStockQuantity;


    public ProductStatusReport(ProductStatus productStatus, Long productCount, Double avgStockQuantity) {
        this(LocalDate.now(),
                productStatus,
                productCount,
                avgStockQuantity);
    }

}
