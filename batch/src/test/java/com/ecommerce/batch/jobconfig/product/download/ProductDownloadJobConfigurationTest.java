package com.ecommerce.batch.jobconfig.product.download;

import com.ecommerce.batch.BaseBatchIntergrationTest;
import com.ecommerce.batch.domain.product.entity.Product;
import com.ecommerce.batch.domain.product.ProductStatus;
import com.ecommerce.batch.service.product.ProductService;
import com.ecommerce.batch.util.DateUtils;
import com.ecommerce.batch.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestPropertySource(properties = {"spring.batch.job.name=productDownloadJob"})
class ProductDownloadJobConfigurationTest extends BaseBatchIntergrationTest {

    @Value("classpath:/data/products_downloaded_expected.csv")
    Resource expectedResource;

    File outputFile;

    @Autowired
    ProductService productService;

    @Test
    void testJob(
            @Qualifier("productDownloadJob")
            @Autowired Job productDownloadJob) throws Exception {
        saveProductEntity();
        outputFile = FileUtils.createTempFile("products_downloaded", ".csv");
        JobParameters jobParameters = jobParameters();
        jobLauncherTestUtils.setJob(productDownloadJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertAll(
                () -> assertThat(Files.readString(Path.of(outputFile.getPath())))
                        .isEqualTo(Files.readString(Path.of(expectedResource.getFile().getPath()))),
                () -> assertJobCompleted(jobExecution)
        );
    }

    private JobParameters jobParameters() {
        return new JobParametersBuilder().addJobParameter("outputFilePath",
                        new JobParameter<>(outputFile.getPath(), String.class, false))
                .addJobParameter("gridSize", new JobParameter<Integer>(2, Integer.class, false))
                .toJobParameters();
    }

    private void saveProductEntity() {
        /*
        productId,sellerId,category,productName,salesStartDate,salesEndDate,productStatus,brand,manufacturer,salesPrice,stockQuantity,createdAt,updatedAt
        1,1,스포츠,축구화,2021-10-10,2024-09-01,DISCONTINUED,미즈노,미즈노,103155,472, 2024-11-17 18:23:59.404,2024-11-17 18:23:59.404
        2,2,화장품,수분크림,2020-07-27,2025-12-25,AVAILABLE,닥터지,아모레퍼시픽,217051,82, 2024-11-18 18:23:59.404,2024-11-18 18:23:59.404
         */
        productService.save(Product.of(
                "1", 1L, "스포츠", "축구화",
                LocalDate.of(2021, 10, 10), LocalDate.of(2024, 9, 1),
                ProductStatus.DISCONTINUED.name(), "미즈노", "미즈노", 103155, 472,
                DateUtils.parseToLocalDateTime("2024-11-17 18:23:59.404"),
                DateUtils.parseToLocalDateTime("2024-11-17 18:23:59.404"))
        );
        productService.save(Product.of(
                "2", 2L, "화장품", "수분크림",
                LocalDate.of(2020, 7, 27), LocalDate.of(2025, 12, 25),
                ProductStatus.AVAILABLE.name(), "닥터지", "아모레퍼시픽", 217051, 82,
                DateUtils.parseToLocalDateTime("2024-11-18 18:23:59.404"),
                DateUtils.parseToLocalDateTime("2024-11-18 18:23:59.404"))
        );

    }


}