package com.ecommerce.batch.service.product.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchJobExecutionListener implements JobExecutionListener {


    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job: {} before execute", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Job: {} after execute {}", jobExecution.getJobInstance().getJobName(), jobExecution.getExecutionContext());
    }
}
