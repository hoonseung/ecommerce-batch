package com.ecommerce.batch.jobconfig.product.report.flow;

import com.ecommerce.batch.domain.product.report.ProductStatusReport;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ProductStatusReportFlowConfiguration {


    @Bean
    public Flow productStatusReportFlow(@Qualifier("productStatusReportStep") Step productStatusReportStep) {
        return new FlowBuilder<SimpleFlow>("productStatusReportFlow")
                .start(productStatusReportStep)
                .build();
    }

    @Bean
    public Step productStatusReportStep(JobRepository jobRepository,
                                        PlatformTransactionManager transactionManager,
                                        StepExecutionListener stepExecutionListener,
                                        ItemReader<ProductStatusReport> productStatusReportReader,
                                        ItemWriter<ProductStatusReport> productStatusReportWriter) {
        return new StepBuilder("productStatusReportStep", jobRepository)
                .<ProductStatusReport, ProductStatusReport>chunk(10, transactionManager)
                .listener(stepExecutionListener)
                .allowStartIfComplete(true)
                .reader(productStatusReportReader)
                .writer(productStatusReportWriter)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<ProductStatusReport> productStatusReportReader(DataSource dataSource) {
        String sql = """
                SELECT product_status,
                       COUNT(*) as product_count,
                       AVG(stock_quantity) as avg_stock_quantity
                FROM products
                GROUP BY product_status;
                """;
        return new JdbcCursorItemReaderBuilder<ProductStatusReport>()
                .name("productStatusReportReader")
                .dataSource(dataSource)
                .sql(sql)
                .beanRowMapper(ProductStatusReport.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<ProductStatusReport> productStatusReportWriter(DataSource dataSource) {
        String sql = """
                INSERT INTO product_status_reports(stat_date,
                                           product_status,
                                           product_count,
                                           avg_stock_quantity)
                VALUES (:statDate, :productStatus, :productCount, :avgStockQuantity);
                """;
        return new JdbcBatchItemWriterBuilder<ProductStatusReport>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
}
