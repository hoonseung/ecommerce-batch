package com.ecommerce.batch.dto;

public record ProductUploadCsvRow(
    Long sellerId,

    String category,
    String productName,
    String salesStartDate,
    String salesEndDate,
    String productStatus,
    String brand,
    String manufacturer,

    int salesPrice,
    int stockQuantity
) {

    public static ProductUploadCsvRow of(
        Long sellerId,
        String category,
        String productName,
        String salesStartDate,
        String salesEndDate,
        String productStatus,
        String brand,
        String manufacturer,
        int salesPrice,
        int stockQuantity
    ) {
        return new ProductUploadCsvRow(
            sellerId,
            category,
            productName,
            salesStartDate,
            salesEndDate,
            productStatus,
            brand,
            manufacturer,
            salesPrice,
            stockQuantity
        );
    }

}
