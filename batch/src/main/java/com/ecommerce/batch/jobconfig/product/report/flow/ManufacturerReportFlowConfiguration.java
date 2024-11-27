package com.ecommerce.batch.jobconfig.product.report.flow;

import com.ecommerce.batch.domain.product.report.ManufacturerReport;
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
    public JdbcCursorItemReader<ManufacturerReport> manufacturerReportReader(DataSource dataSource) {
        String sql = """
                SELECT manufacturer,
                       COUNT(*)                          as product_count,
                       AVG(sales_price)                  as avg_sales_price,
                       SUM(sales_price * stock_quantity) as potential_sales_amount
                FROM products
                GROUP BY manufacturer;
                """;
        return new JdbcCursorItemReaderBuilder<ManufacturerReport>()
                .name("manufacturerReportReader")
                .dataSource(dataSource)
                .sql(sql)
                .beanRowMapper(ManufacturerReport.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<ManufacturerReport> manufacturerReportWriter(DataSource dataSource) {
        String sql = """
                INSERT INTO manufacturer_reports(stat_date,
                                           manufacturer,
                                           product_count,
                                           avg_sales_price,
                                           potential_sales_amount)
                VALUES (:statDate, :manufacturer, :productCount, :avgSalesPrice, :potentialSalesAmount);
                """;
        return new JdbcBatchItemWriterBuilder<ManufacturerReport>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
}
