package com.ecommerce.batch.jobconfig.product.report.flow;

import com.ecommerce.batch.domain.product.report.entity.BrandReport;
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
public class BrandReportFlowConfiguration {


    @Bean
    public Flow brandReportFlow(@Qualifier("brandReportStep") Step brandReportStep) {
        return new FlowBuilder<SimpleFlow>("brandReportFlow")
                .start(brandReportStep)
                .build();
    }

    @Bean
    public Step brandReportStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager,
                                StepExecutionListener stepExecutionListener,
                                ItemReader<BrandReport> brandReportReader,
                                ItemWriter<BrandReport> brandReportWriter) {
        return new StepBuilder("brandReportStep", jobRepository)
                .<BrandReport, BrandReport>chunk(10, transactionManager)
                .listener(stepExecutionListener)
                .allowStartIfComplete(true)
                .reader(brandReportReader)
                .writer(brandReportWriter)
                .build();
    }

    @Bean
    public JpaCursorItemReader<BrandReport> brandReportReader(EntityManagerFactory entityManagerFactory) {
        String sql = "SELECT new BrandReport(p.brand, COUNT(p), AVG(p.salesPrice), MAX(p.salesPrice)," +
                " MIN(p.salesPrice), SUM(p.stockQuantity), AVG(p.stockQuantity), SUM(p.salesPrice * p.stockQuantity))" +
                "FROM Product p GROUP BY p.brand";
        return new JpaCursorItemReaderBuilder<BrandReport>()
                .name("brandReportReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(sql)
                .build();
    }

    @Bean
    public JpaItemWriter<BrandReport> brandReportWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<BrandReport>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(false)
                .build();
    }
}
