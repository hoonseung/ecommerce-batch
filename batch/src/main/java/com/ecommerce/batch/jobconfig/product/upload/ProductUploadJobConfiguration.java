package com.ecommerce.batch.jobconfig.product.upload;

import com.ecommerce.batch.domain.product.Product;
import com.ecommerce.batch.dto.ProductUploadCsvRow;
import com.ecommerce.batch.util.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class ProductUploadJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobExecutionListener jobExecutionListener;
    private final StepExecutionListener stepExecutionListener;


    @Bean
    public Job productUploadJob(Step productUploadStep) {
        return new JobBuilder("productUploadJob", jobRepository)
                .listener(jobExecutionListener)
                .start(productUploadStep)
                .build();
    }

    @Bean
    public Step productUploadStep(ItemReader<ProductUploadCsvRow> productUploadCsvRowItemReader,
                                  ItemProcessor<ProductUploadCsvRow, Product> productItemProcessor,
                                  ItemWriter<Product> productItemWriter) {
        return new StepBuilder("productUploadStep", jobRepository)
                .<ProductUploadCsvRow, Product>chunk(1000, platformTransactionManager)
                .allowStartIfComplete(true)
                .listener(stepExecutionListener)
                .reader(productUploadCsvRowItemReader)
                .processor(productItemProcessor)
                .writer(productItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<ProductUploadCsvRow> productUploadCsvRowItemReader(
            @Value("#{jobParameters['inputFilePath']}") String path) {
        return new FlatFileItemReaderBuilder<ProductUploadCsvRow>()
                .resource(new FileSystemResource(path))
                .name("productUploadCsvRowItemReader")
                .delimited().delimiter(",")
                .names(ReflectionUtils.getFieldNames(ProductUploadCsvRow.class))
                .targetType(ProductUploadCsvRow.class)
                .linesToSkip(1)
                .build();
    }

    @Bean
    public ItemProcessor<ProductUploadCsvRow, Product> productItemProcessor() {
        return Product::from;
    }


    @Bean
    public ItemWriter<Product> productItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Product>()
                .dataSource(dataSource)
                .sql("INSERT INTO products(product_id, seller_id, category, product_name, sales_start_date, " +
                        "sales_end_date, product_status, brand, manufacturer, sales_price, stock_quantity, created_at, updated_at) " +
                        "VALUES (:productId, :sellerId, :category, :productName, :salesStartDate, :salesEndDate, :productStatus, :brand, :manufacturer, :salesPrice, :stockQuantity, :createdAt, :updatedAt)"
                )
                .beanMapped()
                .build();
    }
}
