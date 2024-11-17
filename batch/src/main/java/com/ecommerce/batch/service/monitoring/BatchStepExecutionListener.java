package com.ecommerce.batch.service.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchStepExecutionListener implements StepExecutionListener, ChunkListener {

    private final CustomPrometheusPushGatewayManager prometheusPushGatewayManager;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("after step execution context: {}", stepExecution.getExecutionContext());
        prometheusPushGatewayManager.pushMetrics(Map.of("job_name", stepExecution.getJobExecution().getJobInstance().getJobName()));
        return stepExecution.getExitStatus();
    }

    @Override
    public void afterChunk(ChunkContext context) {
        prometheusPushGatewayManager.pushMetrics(Map.of("job_name", context.getStepContext().getJobName()));
    }
}
