package com.ecommerce.batch.jobconfig.product.download;

import com.ecommerce.batch.domain.file.PartitionedFileRepository;
import com.ecommerce.batch.domain.product.Product;
import com.ecommerce.batch.dto.ProductDownloadCsvRow;
import com.ecommerce.batch.service.product.ProductDownloadPartitioner;
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
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.util.Map;

@Configuration
public class ProductDownloadJobConfiguration {

    @Bean
    public Job productDownloadJob(JobRepository jobRepository,
                                  @Qualifier("productDownloadPartitionStep")
                                  Step productDownloadPartitionStep,
                                  @Qualifier("productFileMergeStep")
                                  Step productFileMergeStep,
                                  JobExecutionListener jobExecutionListener) {
        return new JobBuilder("productDownloadJob", jobRepository)
                .listener(jobExecutionListener)
                .start(productDownloadPartitionStep)
                .next(productFileMergeStep)
                .build();
    }

    @Bean
    public Step productDownloadPartitionStep(JobRepository jobRepository,
                                             @Qualifier("productDownloadPartitionHandler")
                                             PartitionHandler productDownloadPartitionHandler,
                                             @Qualifier("productPagingStep")
                                             Step productPagingStep,
                                             @Qualifier("productDownloadPartitioner")
                                             ProductDownloadPartitioner productDownloadPartitioner) {
        return new StepBuilder("productDownloadPartitionStep", jobRepository)
                .partitioner(productPagingStep.getName(), productDownloadPartitioner)
                .partitionHandler(productDownloadPartitionHandler)
                .allowStartIfComplete(true)
                .build();
    }


    @Bean
    @JobScope
    public TaskExecutorPartitionHandler productDownloadPartitionHandler(
            @Value("#{jobParameters['gridSize']}") int gridSize,
            @Qualifier("productPagingStep")
            Step productPagingStep,
            TaskExecutor threadPoolTaskExecutor) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setGridSize(gridSize);
        handler.setStep(productPagingStep);
        handler.setTaskExecutor(threadPoolTaskExecutor);
        return handler;
    }


    @Bean
    public Step productPagingStep(JobRepository jobRepository,
                                  PlatformTransactionManager platformTransactionManager,
                                  StepExecutionListener stepExecutionListener,
                                  ItemReader<Product> productPagingItemReader,
                                  ItemProcessor<Product, ProductDownloadCsvRow> productDownloadProcessor,
                                  ItemWriter<ProductDownloadCsvRow> productDownloadFileWriter,
                                  TaskExecutor threadPoolTaskExecutor) {
        return new StepBuilder("productPagingStep", jobRepository)
                .<Product, ProductDownloadCsvRow>chunk(10000, platformTransactionManager)
                .listener(stepExecutionListener)
                .allowStartIfComplete(true)
                .reader(productPagingItemReader)
                .processor(productDownloadProcessor)
                .writer(productDownloadFileWriter)
                .taskExecutor(threadPoolTaskExecutor)
                .build();
    }

    @Bean
    public Step productFileMergeStep(JobRepository jobRepository,
                                     PlatformTransactionManager platformTransactionManager,
                                     Tasklet productFileMergeTasklet,
                                     StepExecutionListener stepExecutionListener) {
        return new StepBuilder("productFileMergeStep", jobRepository)
                .allowStartIfComplete(true)
                .listener(stepExecutionListener)
                .tasklet(productFileMergeTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Tasklet productFileMergeTasklet(
            @Value("#{jobParameters['outputFilePath']}") String path,
            PartitionedFileRepository repository) {
        return (contribution, chunkContext) -> {
            FileUtils.mergeFiles(repository.getFiles(), path);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<Product> productPagingItemReader(
            @Value("#{stepExecutionContext['minId']}") String minId,
            @Value("#{stepExecutionContext['maxId']}") String maxId,
            DataSource dataSource,
            PagingQueryProvider productPagingQueryProvider) {
        return new JdbcPagingItemReaderBuilder<Product>()
                .name("productItemReader")
                .dataSource(dataSource)
                .parameterValues(Map.of("minId", minId, "maxId", maxId))
                .queryProvider(productPagingQueryProvider)
                .beanRowMapper(Product.class)
                .pageSize(1000)
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean productPagingQueryProvider(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(dataSource);
        provider.setSelectClause("product_id, seller_id, category, product_name, sales_start_date, sales_end_date, product_status, " +
                "brand, manufacturer, sales_price, stock_quantity, created_at, updated_at");
        provider.setFromClause("from products");
        provider.setWhereClause("where product_id >= :minId and product_id <= :maxId");
        provider.setSortKey("product_id");
        return provider;
    }

    @Bean
    public ItemProcessor<Product, ProductDownloadCsvRow> productDownloadProcessor() {
        return ProductDownloadCsvRow::from;
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamWriter<ProductDownloadCsvRow> productDownloadFileWriter(
            @Value("#{stepExecutionContext['file']}") File file) {
        String[] fieldNames = ReflectionUtils.getFieldNames(ProductDownloadCsvRow.class);
        FlatFileItemWriter<ProductDownloadCsvRow> flatFileItemWriter = new FlatFileItemWriterBuilder<ProductDownloadCsvRow>()
                .name("productDownloadFileWriter")
                .resource(new FileSystemResource(file))
                .shouldDeleteIfExists(true)
                .delimited().delimiter(",")
                .names(fieldNames)
                .build();

        return new SynchronizedItemStreamWriterBuilder<ProductDownloadCsvRow>()
                .delegate(flatFileItemWriter)
                .build();
    }


}
