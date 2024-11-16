package com.ecommerce.batch.service.product.monitoring;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusPushGatewayManager;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
public class CustomPrometheusPushGatewayManager extends PrometheusPushGatewayManager {

    private final PushGateway pushGateway;
    private final CollectorRegistry collectorRegistry;
    private final String job;

    public CustomPrometheusPushGatewayManager(PushGateway pushGateway, CollectorRegistry registry, @Value("${prometheus.job.name:spring-batch}") String job) {
        super(pushGateway, registry, Duration.ofSeconds(30), job, Map.of(), ShutdownOperation.POST);
        this.pushGateway = pushGateway;
        this.collectorRegistry = registry;
        this.job = job;
    }

    public void pushMetrics(Map<String, String> groupingKeys) {
        try {
            log.info("pushgateway push add");
            pushGateway.pushAdd(collectorRegistry, job, groupingKeys);
        } catch (IOException e) {
            log.error("e: {}", e.getMessage(), e);
        }
    }
}
