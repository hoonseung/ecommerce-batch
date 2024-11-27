package com.ecommerce.batch.jobconfig.product.report.flow;

import com.ecommerce.batch.domain.product.report.CategoryReport;
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
    public JdbcCursorItemReader<CategoryReport> categoryReportReader(DataSource dataSource) {
        String sql = """
                SELECT category,
                       COUNT(*)                          as product_count,
                       AVG(sales_price)                  as avg_sales_price,
                       MAX(sales_price)                  as max_sales_price,
                       MIN(sales_price)                  as min_sales_price,
                       SUM(stock_quantity)               as total_stock_quantity,
                       SUM(sales_price * stock_quantity) as potential_sales_amount
                FROM products
                GROUP BY category;
                """;
        return new JdbcCursorItemReaderBuilder<CategoryReport>()
                .name("categoryReportReader")
                .dataSource(dataSource)
                .sql(sql)
                .beanRowMapper(CategoryReport.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<CategoryReport> categoryReportWriter(DataSource dataSource) {
        String sql = """
                INSERT INTO category_reports (stat_date,
                                           category,
                                           product_count,
                                           avg_sales_price,
                                           max_sales_price,
                                           min_sales_price,
                                           total_stock_quantity,
                                           potential_sales_amount)
                VALUES (:statDate, :category, :productCount, :avgSalesPrice, :maxSalesPrice, :minSalesPrice, :totalStockQuantity, :potentialSalesAmount);
                """;
        return new JdbcBatchItemWriterBuilder<CategoryReport>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }
}
