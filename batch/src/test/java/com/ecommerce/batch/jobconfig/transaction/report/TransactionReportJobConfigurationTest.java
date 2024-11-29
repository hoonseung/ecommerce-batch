package com.ecommerce.batch.jobconfig.transaction.report;

import com.ecommerce.batch.BaseBatchIntergrationTest;
import com.ecommerce.batch.domain.transaction.report.repository.TransactionReportRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = {"spring.batch.job.name=transactionReportJob"})
class TransactionReportJobConfigurationTest extends BaseBatchIntergrationTest {

    @Value("classpath:logs/transaction.log")
    private Resource resource;
    @Autowired
    private TransactionReportRepository transactionReportRepository;

    @Test
    void testJob(@Autowired @Qualifier("transactionReportJob") Job transactionReportJob) throws Exception {
        jobLauncherTestUtils.setJob(transactionReportJob);
        JobParameters jobParameters = getJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Assertions.assertAll(
                () -> assertJobCompleted(jobExecution),
                () -> assertThat(transactionReportRepository.count()).isEqualTo(3)
        );
    }

    private JobParameters getJobParameters() throws IOException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addJobParameter("inputFilePath", new JobParameter<String>(resource.getFile().getPath(), String.class, false))
                .addJobParameter("gridSize", new JobParameter<>(3, Integer.class, false));
        return jobParametersBuilder.toJobParameters();
    }

}