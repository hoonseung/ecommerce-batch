package com.ecommerce.batch.domain.product.report;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryReport {
    private LocalDate statDate = LocalDate.now();

    private String category;
    private Long productCount;
    private BigDecimal avgSalesPrice;
    private BigDecimal maxSalesPrice;
    private BigDecimal minSalesPrice;
    private Integer totalStockQuantity;
    private BigDecimal potentialSalesAmount;


}
