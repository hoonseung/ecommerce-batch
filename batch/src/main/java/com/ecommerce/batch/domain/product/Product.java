package com.ecommerce.batch.domain.product;


import com.ecommerce.batch.dto.ProductUploadCsvRow;
import com.ecommerce.batch.util.DateUtils;
import com.ecommerce.batch.util.RandomUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    private String productId;
    private Long sellerId;

    private String category;
    private String productName;
    private LocalDate salesStartDate;
    private LocalDate salesEndDate;
    private String productStatus;
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
                row.productStatus(),
                row.brand(),
                row.manufacturer(),
                row.salesPrice(),
                row.stockQuantity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
