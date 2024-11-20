package com.ecommerce.batch.dto;

import com.ecommerce.batch.domain.product.Product;
import com.ecommerce.batch.util.DateUtils;

public record ProductDownloadCsvRow(
        String productId,
        Long sellerId,

        String category,
        String productName,
        String salesStartDate,
        String salesEndDate,
        String productStatus,
        String brand,
        String manufacturer,

        int salesPrice,
        int stockQuantity,
        String createdAt,
        String updatedAt
) {

    public static ProductDownloadCsvRow from(Product product) {
        return new ProductDownloadCsvRow(
                product.getProductId(),
                product.getSellerId(),
                product.getCategory(),
                product.getProductName(),
                DateUtils.formatToString(product.getSalesStartDate()),
                DateUtils.formatToString(product.getSalesEndDate()),
                product.getProductStatus(),
                product.getBrand(),
                product.getManufacturer(),
                product.getSalesPrice(),
                product.getStockQuantity(),
                DateUtils.formatToString(product.getCreatedAt()),
                DateUtils.formatToString(product.getUpdatedAt())
        );
    }

}
