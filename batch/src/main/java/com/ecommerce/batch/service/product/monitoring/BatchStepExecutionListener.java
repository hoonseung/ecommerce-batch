package com.ecommerce.batch.service.product.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchStepExecutionListener implements StepExecutionListener {

    private final CustomPrometheusPushGatewayManager prometheusPushGatewayManager;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("after step execution context: {}", stepExecution.getExecutionContext());
        prometheusPushGatewayManager.pushMetrics(Map.of("job_name", stepExecution.getJobExecution().getJobInstance().getJobName()));
        return stepExecution.getExitStatus();
    }
}
