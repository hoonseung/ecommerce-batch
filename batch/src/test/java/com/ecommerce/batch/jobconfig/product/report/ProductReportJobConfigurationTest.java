package com.ecommerce.batch.jobconfig.product.report;

import com.ecommerce.batch.BaseBatchIntergrationTest;
import com.ecommerce.batch.domain.product.ProductStatus;
import com.ecommerce.batch.domain.product.entity.Product;
import com.ecommerce.batch.service.product.ProductService;
import com.ecommerce.batch.service.product.report.ProductReportService;
import com.ecommerce.batch.util.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = "spring.batch.job.name=productReportJob")
class ProductReportJobConfigurationTest extends BaseBatchIntergrationTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductReportService productReportService;

    @Test
    void testJob(@Autowired @Qualifier("productReportJob") Job productReportJob) throws Exception {
        saveProductEntity();

        jobLauncherTestUtils.setJob(productReportJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        Assertions.assertAll(
                () -> assertThat(productReportService.getCategoryReportCount(now())).isEqualTo(2L),
                () -> assertThat(productReportService.getBrandReportCount(now())).isEqualTo(2L),
                () -> assertThat(productReportService.getProductStatusReportCount(now())).isEqualTo(2L),
                () -> assertThat(productReportService.getManufacturerReportCount(now())).isEqualTo(2L),
                () -> assertJobCompleted(jobExecution)
        );
    }

    private void saveProductEntity() {
        productService.save(Product.of(
                "1", 1L, "스포츠", "축구화",
                LocalDate.of(2021, 10, 10), LocalDate.of(2024, 9, 1),
                ProductStatus.DISCONTINUED.name(), "미즈노", "미즈노", 103155, 472,
                DateUtils.parseToLocalDateTime("2024-11-17 18:23:59.404"),
                DateUtils.parseToLocalDateTime("2024-11-17 18:23:59.404"))
        );
        productService.save(Product.of(
                "2", 2L, "화장품", "수분크림",
                LocalDate.of(2020, 7, 27), LocalDate.of(2024, 9, 1),
                ProductStatus.AVAILABLE.name(), "닥터지", "아모레퍼시픽", 217051, 82,
                DateUtils.parseToLocalDateTime("2024-11-18 18:23:59.404"),
                DateUtils.parseToLocalDateTime("2024-11-18 18:23:59.404"))
        );

    }


}