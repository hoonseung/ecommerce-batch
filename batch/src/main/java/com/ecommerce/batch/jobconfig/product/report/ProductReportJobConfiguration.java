package com.ecommerce.batch.jobconfig.product.report;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;


@Configuration
public class ProductReportJobConfiguration {

    @Bean
    public Job productReportJob(JobRepository jobRepository,
                                JobExecutionListener jobExecutionListener,
                                @Qualifier("categoryReportFlow")
                                Flow categoryReportFlow,
                                @Qualifier("brandReportFlow")
                                Flow brandReportFlow,
                                @Qualifier("manufacturerReportFlow")
                                Flow manufacturerReportFlow,
                                @Qualifier("productStatusReportFlow")
                                Flow productStatusReportFlow,
                                TaskExecutor threadPoolTaskExecutor) {
        return new JobBuilder("productReportJob", jobRepository)
                .listener(jobExecutionListener)
                .start(categoryReportFlow)
                .split(threadPoolTaskExecutor)
                .add(brandReportFlow, manufacturerReportFlow, productStatusReportFlow)
                .end()
                .build();
    }
}
