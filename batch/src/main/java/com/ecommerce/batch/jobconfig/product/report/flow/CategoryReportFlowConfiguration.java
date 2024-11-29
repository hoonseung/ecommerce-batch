package com.ecommerce.batch.jobconfig.product.report.flow;

import com.ecommerce.batch.domain.product.report.entity.CategoryReport;
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
public class CategoryReportFlowConfiguration {


    @Bean
    public Flow categoryReportFlow(@Qualifier("categoryReportStep") Step categoryReportStep) {
        return new FlowBuilder<SimpleFlow>("categoryReportFlow")
                .start(categoryReportStep)
                .build();
    }

    @Bean
    public Step categoryReportStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   StepExecutionListener stepExecutionListener,
                                   ItemReader<CategoryReport> categoryReportReader,
                                   ItemWriter<CategoryReport> categoryReportWriter) {
        return new StepBuilder("categoryReportStep", jobRepository)
                .<CategoryReport, CategoryReport>chunk(10, transactionManager)
                .listener(stepExecutionListener)
                .allowStartIfComplete(true)
                .reader(categoryReportReader)
                .writer(categoryReportWriter)
                .build();
    }

    @Bean
    public JpaCursorItemReader<CategoryReport> categoryReportReader(EntityManagerFactory entityManagerFactory) {
        String sql = "SELECT new CategoryReport (p.category, COUNT(p), AVG(p.salesPrice), MAX(p.salesPrice), " +
                "MIN(p.salesPrice), SUM(p.stockQuantity), SUM(p.salesPrice * p.stockQuantity)) " +
                "FROM Product p GROUP BY p.category";
        return new JpaCursorItemReaderBuilder<CategoryReport>()
                .name("categoryReportReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(sql)
                .build();
    }

    @Bean
    public JpaItemWriter<CategoryReport> categoryReportWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<CategoryReport>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(false)
                .build();
    }
}
