package com.ecommerce.batch.domain.product.entity;


import com.ecommerce.batch.domain.product.ProductStatus;
import com.ecommerce.batch.dto.product.ProductUploadCsvRow;
import com.ecommerce.batch.util.DateUtils;
import com.ecommerce.batch.util.RandomUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
@Entity
public class Product {

    @Id
    private String productId;
    private Long sellerId;

    private String category;
    private String productName;
    private LocalDate salesStartDate;
    private LocalDate salesEndDate;
    @Enumerated(STRING)
    private ProductStatus productStatus;
    private String brand;
    private String manufacturer;

    private int salesPrice;
    private int stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static Product from(ProductUploadCsvRow row) {
        return new Product(
                RandomUtils.generateRandomProductId(),
                row.sellerId(),
                row.category(),
                row.productName(),
                DateUtils.parseToLocalDate(row.salesStartDate()),
                DateUtils.parseToLocalDate(row.salesEndDate()),
                ProductStatus.valueOf(row.productStatus()),
                row.brand(),
                row.manufacturer(),
                row.salesPrice(),
                row.stockQuantity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static Product of(String productId, Long sellerId, String category, String productName,
                             LocalDate salesStartDate, LocalDate salesEndDate, String productStatus,
                             String brand, String manufacturer, int salesPrice, int stockQuantity,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Product(
                productId,
                sellerId,
                category,
                productName,
                salesStartDate,
                salesEndDate,
                ProductStatus.valueOf(productStatus),
                brand,
                manufacturer,
                salesPrice,
                stockQuantity,
                createdAt,
                updatedAt);
    }
}
