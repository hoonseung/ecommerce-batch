package com.ecommerce.batch.jobconfig.product.report.flow;

import com.ecommerce.batch.domain.product.report.entity.ManufacturerReport;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ManufacturerReportFlowConfiguration {


    @Bean
    public Flow manufacturerReportFlow(@Qualifier("manufacturerReportStep") Step manufacturerReportStep) {
        return new FlowBuilder<SimpleFlow>("manufacturerReportFlow")
                .start(manufacturerReportStep)
                .build();
    }

    @Bean
    public Step manufacturerReportStep(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       StepExecutionListener stepExecutionListener,
                                       ItemReader<ManufacturerReport> manufacturerReportReader,
                                       ItemWriter<ManufacturerReport> manufacturerReportWriter) {
        return new StepBuilder("manufacturerReportStep", jobRepository)
                .<ManufacturerReport, ManufacturerReport>chunk(10, transactionManager)
                .listener(stepExecutionListener)
                .allowStartIfComplete(true)
                .reader(manufacturerReportReader)
                .writer(manufacturerReportWriter)
                .build();
    }

    @Bean
    public JpaCursorItemReader<ManufacturerReport> manufacturerReportReader(EntityManagerFactory entityManagerFactory) {
        String sql = "SELECT new ManufacturerReport(p.manufacturer, COUNT(p), AVG(p.salesPrice), SUM(p.salesPrice * p.stockQuantity))" +
                " FROM Product p GROUP BY p.manufacturer";
        return new JpaCursorItemReaderBuilder<ManufacturerReport>()
                .name("manufacturerReportReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(sql)
                .build();
    }

    @Bean
    public JpaItemWriter<ManufacturerReport> manufacturerReportWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<ManufacturerReport>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(false)
                .build();
    }
}
