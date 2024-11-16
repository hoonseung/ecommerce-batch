package com.ecommerce.batch.service.product.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchJobExecutionListener implements JobExecutionListener {

    private final CustomPrometheusPushGatewayManager prometheusPushGatewayManager;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job: {} before execute", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        log.info("Job: {} after execute {}", jobName, jobExecution.getExecutionContext());
        prometheusPushGatewayManager.pushMetrics(Map.of("job_name", jobName));
    }
}
