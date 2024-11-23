package com.ecommerce.api.domain.product.dto;

import com.ecommerce.api.domain.product.ProductStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductResponse(
        String productId,
        Long sellerId,
        String category,
        String productName,
        LocalDate salesStartDate,
        LocalDate salesEndDate,
        ProductStatus productStatus,
        String brand,
        String manufacturer,
        int salesPrice,
        int stockQuantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {


    public static ProductResponse from(ProductResult productResult) {
        return new ProductResponse(
                productResult.productId(),
                productResult.sellerId(),
                productResult.category(),
                productResult.productName(),
                productResult.salesStartDate(),
                productResult.salesEndDate(),
                productResult.productStatus(),
                productResult.brand(),
                productResult.manufacturer(),
                productResult.salesPrice(),
                productResult.stockQuantity(),
                productResult.createdAt(),
                productResult.updatedAt()
        );
    }

}
