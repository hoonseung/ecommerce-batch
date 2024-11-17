package com.ecommerce.batch.jobconfig.product.upload;

import com.ecommerce.batch.BaseBatchIntergrationTest;
import com.ecommerce.batch.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@TestPropertySource(properties = {"spring.batch.job.name=productUploadJob"})
class ProductUploadJobConfigurationTest extends BaseBatchIntergrationTest {

    @Autowired
    private ProductService productService;

    @Value("classpath:/data/products_for_upload.csv")
    private Resource input;


    @Test
    void testJob(@Autowired Job productUploadJob) throws Exception {
        JobParameters jobParameters = jobParameters();
        jobLauncherTestUtils.setJob(productUploadJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);


        assertAll(() -> assertThat(productService.getCountsForProduct()).isEqualTo(6),
                () -> assertJobCompleted(jobExecution)
        );
    }


    private JobParameters jobParameters() throws IOException {
        return new JobParametersBuilder().addJobParameter("inputFilePath",
                        new JobParameter<>(input.getFile().getPath(), String.class, false))
                .addJobParameter("gridSize", new JobParameter<>(3, Integer.class, false))
                .toJobParameters();
    }


}