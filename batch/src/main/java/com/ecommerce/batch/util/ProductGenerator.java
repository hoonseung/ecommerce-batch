package com.ecommerce.batch.util;

import com.ecommerce.batch.domain.product.ProductStatus;
import com.ecommerce.batch.dto.ProductUploadCsvRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

@Slf4j
public class ProductGenerator {

    private static final Random RANDOM = new Random();
    private static final String[] CATEGORIES = {"가전", "가구", "의류", "식품", "화장품", "장난감", "서적", "디지털",
            "스포츠", "신발"};
    private static final String[] PRODUCT_NAMES = {"TV", "침대", "재킷", "고기", "수분크림", "레고", "좋은 소설",
            "노트북", "축구화", "런닝화"};
    private static final String[] BRANDS = {"삼성", "시몬스", "네버뎃", "아임닭", "닥터지", "코나미", "좋은사", "LG",
            "미즈노", "나이키"};
    private static final String[] MANUFACTURES = {"삼성전자", "시몬스", "디스이즈네버넷", "하림", "아모레퍼시픽", "코나미",
            "좋은", "LG", "미즈노", "나이키"};
    private static final String[] PRODUCT_STATUSES = Arrays.stream(ProductStatus.values())
            .map(String::valueOf).toArray(String[]::new);

    public static void main(String[] args) {
        String csvFilePath = "data/random_product.csv";
        int recordCount = 10_000_000;

        try (FileWriter writer = new FileWriter(csvFilePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                     .setHeader(ReflectionUtils.getFieldNames(ProductUploadCsvRow.class)).build())) {

            for (int i = 0; i < recordCount; i++) {
                csvPrinter.printRecord(generateRecords());
                if (i % 100_000 == 0) {
                    log.info("Generated {} records", i);
                }
            }

        } catch (IOException e) {
            log.error("error message: {}", e.getMessage());
        }


    }


    private static Object[] generateRecords() {
        var productUploadCsvRow = randomProductRow();
        return new Object[]{
                productUploadCsvRow.sellerId(),
                productUploadCsvRow.category(),
                productUploadCsvRow.productName(),
                productUploadCsvRow.salesStartDate(),
                productUploadCsvRow.salesEndDate(),
                productUploadCsvRow.productStatus(),
                productUploadCsvRow.brand(),
                productUploadCsvRow.manufacturer(),
                productUploadCsvRow.salesPrice(),
                productUploadCsvRow.stockQuantity()
        };
    }

    private static ProductUploadCsvRow randomProductRow() {
        return ProductUploadCsvRow.of(
                randomSellerId(),
                randomSelect(CATEGORIES),
                randomSelect(PRODUCT_NAMES),
                randomDate(2020, 2023),
                randomDate(2024, 2026),
                randomSelect(PRODUCT_STATUSES),
                randomSelect(BRANDS),
                randomSelect(MANUFACTURES),
                randomSalesPrice(),
                randomStockQuantity()
        );
    }

    private static int randomStockQuantity() {
        return RANDOM.nextInt(1, 1001);
    }

    private static int randomSalesPrice() {
        return RANDOM.nextInt(10_000, 500_001);
    }

    private static Long randomSellerId() {
        return RANDOM.nextLong(1, 101);
    }

    private static String randomSelect(String[] array) {
        return array[RANDOM.nextInt(array.length)];
    }

    private static String randomDate(int startYear, int endYear) {
        int year = RANDOM.nextInt(startYear, endYear + 1);
        int month = RANDOM.nextInt(1, 13);
        int day = RANDOM.nextInt(1, 29);

        return LocalDate.of(year, month, day).toString();
    }

}
