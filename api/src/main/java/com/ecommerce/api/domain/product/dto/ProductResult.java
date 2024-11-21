package com.ecommerce.api.domain.product.dto;

import com.ecommerce.api.domain.product.ProductStatus;
import com.ecommerce.api.domain.product.entity.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductResult(
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


    public static ProductResult from(Product product) {
        return new ProductResult(
                product.getProductId(),
                product.getSellerId(),
                product.getCategory(),
                product.getProductName(),
                product.getSalesStartDate(),
                product.getSalesEndDate(),
                product.getProductStatus(),
                product.getBrand(),
                product.getManufacturer(),
                product.getSalesPrice(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

}
