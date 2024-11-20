package com.ecommerce.batch.jobconfig.product.upload;

import com.ecommerce.batch.domain.product.Product;
import com.ecommerce.batch.dto.ProductUploadCsvRow;
import com.ecommerce.batch.service.file.SplitFilePartitioner;
import com.ecommerce.batch.util.FileUtils;
import com.ecommerce.batch.util.ReflectionUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;


@Configuration
public class ProductUploadJobConfiguration {


    @Bean
    public Job productUploadJob(
            JobRepository jobRepository,
            JobExecutionListener jobExecutionListener,
            @Qualifier("productUploadStepPartitionStep")
            Step productUploadStepPartitionStep) {
        return new JobBuilder("productUploadJob", jobRepository)
                .listener(jobExecutionListener)
                .start(productUploadStepPartitionStep)
                .build();
    }

    @Bean
    public Step productUploadStepPartitionStep(
            JobRepository jobRepository,
            @Qualifier("productUploadStep")
            Step productUploadStep,
            @Qualifier("splitFilePartitioner")
            SplitFilePartitioner splitFilePartitioner,
            @Qualifier("filePartitionHandler")
            PartitionHandler filePartitionHandler) {
        return new StepBuilder("productUploadStepPartitionStep", jobRepository)
                .partitioner(productUploadStep.getName(), splitFilePartitioner)
                .partitionHandler(filePartitionHandler)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step productUploadStep(JobRepository jobRepository,
                                  PlatformTransactionManager platformTransactionManager,
                                  StepExecutionListener stepExecutionListener,
                                  ItemReader<ProductUploadCsvRow> productUploadCsvRowItemReader,
                                  ItemProcessor<ProductUploadCsvRow, Product> productItemProcessor,
                                  ItemWriter<Product> productItemWriter,
                                  TaskExecutor threadPoolTaskExecutor) {
        return new StepBuilder("productUploadStep", jobRepository)
                .<ProductUploadCsvRow, Product>chunk(1000, platformTransactionManager)
                .allowStartIfComplete(true)
                .listener(stepExecutionListener)
                .reader(productUploadCsvRowItemReader)
                .processor(productItemProcessor)
                .writer(productItemWriter)
                .taskExecutor(threadPoolTaskExecutor)
                .build();
    }

    @Bean
    @JobScope
    public SplitFilePartitioner splitFilePartitioner(
            @Value("#{jobParameters['inputFilePath']}") String path,
            @Value("#{jobParameters['gridSize']}") int gridSize
    ) {
        return new SplitFilePartitioner(FileUtils.splitCSV(path, gridSize));
    }

    @Bean
    @JobScope
    public TaskExecutorPartitionHandler filePartitionHandler(
            TaskExecutor threadPoolTaskExecutor,
            @Qualifier("productUploadStep")
            Step productUploadStep,
            @Value("#{jobParameters['gridSize']}") int gridSize) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setTaskExecutor(threadPoolTaskExecutor);
        handler.setStep(productUploadStep);
        handler.setGridSize(gridSize);
        return handler;
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<ProductUploadCsvRow> productUploadCsvRowItemReader(@Value("#{stepExecutionContext['file']}") File file) {
        FlatFileItemReader<ProductUploadCsvRow> targetItemReader = new FlatFileItemReaderBuilder<ProductUploadCsvRow>()
                .resource(new FileSystemResource(file))
                .name("productUploadCsvRowItemReader")
                .delimited().delimiter(",")
                .names(ReflectionUtils.getFieldNames(ProductUploadCsvRow.class))
                .targetType(ProductUploadCsvRow.class)
                .build();

        return new SynchronizedItemStreamReaderBuilder<ProductUploadCsvRow>()
                .delegate(targetItemReader)
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
