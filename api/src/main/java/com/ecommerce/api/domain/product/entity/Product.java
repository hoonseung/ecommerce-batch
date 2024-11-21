package com.ecommerce.api.domain.product.entity;


import com.ecommerce.api.domain.product.ProductStatus;
import com.ecommerce.api.domain.product.exeption.InsufficientStockQuantityException;
import com.ecommerce.api.domain.product.exeption.InvalidStockQuantityException;
import com.ecommerce.api.domain.product.exeption.StockQuantityArithmeticException;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Setter
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
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public static Product of(String productId, Long sellerId, String category, String productName,
                             LocalDate salesStartDate, LocalDate salesEndDate, ProductStatus productStatus,
                             String brand, String manufacturer, int salesPrice, int stockQuantity,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Product(
                productId,
                sellerId,
                category,
                productName,
                salesStartDate,
                salesEndDate,
                productStatus,
                brand,
                manufacturer,
                salesPrice,
                stockQuantity,
                createdAt,
                updatedAt);
    }

    public void increaseStockQuantity(int inputStockQuantity) {
        checkingNegative(inputStockQuantity);
        if (this.stockQuantity + inputStockQuantity < 0) {
            throw new StockQuantityArithmeticException();
        }
        this.stockQuantity += inputStockQuantity;
    }

    public void decreaseStockQuantity(int inputStockQuantity) {
        checkingNegative(inputStockQuantity);
        if (this.stockQuantity < inputStockQuantity) {
            throw new InsufficientStockQuantityException();
        }
        this.stockQuantity -= inputStockQuantity;
    }

    private void checkingNegative(int inputStockQuantity) {
        if (inputStockQuantity <= 0) {
            throw new InvalidStockQuantityException();
        }
    }
}
