package com.ecommerce.batch.jobconfig.product.report.flow;

import com.ecommerce.batch.domain.product.report.BrandReport;
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
    public JdbcCursorItemReader<BrandReport> brandReportReader(DataSource dataSource) {
        String sql = """
                SELECT brand,
                       COUNT(*)                          as product_count,
                       AVG(sales_price)                  as avg_sales_price,
                       MAX(sales_price)                  as max_sales_price,
                       MIN(sales_price)                  as min_sales_price,
                       SUM(stock_quantity)               as total_stock_quantity,
                       AVG(stock_quantity)               as avg_stock_quantity,
                       SUM(sales_price * stock_quantity) as potential_sales_amount
                FROM products
                GROUP BY brand
                """;
        return new JdbcCursorItemReaderBuilder<BrandReport>()
                .name("brandReportReader")
                .dataSource(dataSource)
                .sql(sql)
                .beanRowMapper(BrandReport.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BrandReport> brandReportWriter(DataSource dataSource) {
        String sql = """
                INSERT INTO brand_reports (stat_date,
                                           brand,
                                           product_count,
                                           avg_sales_price,
                                           max_sales_price,
                                           min_sales_price,
                                           total_stock_quantity,
                                           avg_stock_quantity,
                                           potential_sales_amount)
                VALUES (:statDate, :brand, :productCount, :avgSalesPrice, :maxSalesPrice, :minSalesPrice,
                        :totalStockQuantity, :avgStockQuantity, :potentialSalesAmount);
                """;
        return new JdbcBatchItemWriterBuilder<BrandReport>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
}
