package com.ecommerce.batch.jobconfig.transaction.report;


import com.ecommerce.batch.domain.transaction.report.TransactionReport;
import com.ecommerce.batch.domain.transaction.report.TransactionReportMemoryRepository;
import com.ecommerce.batch.dto.transaction.log.TransactionLog;
import com.ecommerce.batch.service.file.SplitFilePartitioner;
import com.ecommerce.batch.service.transaction.TransactionReportAccumulator;
import com.ecommerce.batch.util.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.IteratorItemReader;
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
public class TransactionReportJobConfiguration {


    @Bean
    public Job transactionReportJob(JobRepository jobRepository,
                                    JobExecutionListener jobExecutionListener,
                                    @Qualifier("transactionAccPartitionStep")
                                    Step transactionAccPartitionStep,
                                    @Qualifier("transactionSaveStep")
                                    Step transactionSaveStep) {
        return new JobBuilder("transactionReportJob", jobRepository)
                .start(transactionAccPartitionStep)
                .next(transactionSaveStep)
                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    public Step transactionAccPartitionStep(
            JobRepository jobRepository,
            @Qualifier("transactionAccStep")
            Step transactionAccStep,
            @Qualifier("splitLogFilePartitioner")
            SplitFilePartitioner splitLogFilePartitioner,
            @Qualifier("logFilePartitionHandler")
            PartitionHandler logFilePartitionHandler) {
        return new StepBuilder("transactionAccPartitionStep", jobRepository)
                .partitioner(transactionAccStep.getName(), splitLogFilePartitioner)
                .partitionHandler(logFilePartitionHandler)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @JobScope
    public SplitFilePartitioner splitLogFilePartitioner(
            @Value("#{jobParameters['inputFilePath']}") String path,
            @Value("#{jobParameters['gridSize']}") int gridSize
    ) {
        return new SplitFilePartitioner(FileUtils.splitLog(path, gridSize));
    }

    @Bean
    @JobScope
    public TaskExecutorPartitionHandler logFilePartitionHandler(
            TaskExecutor threadPoolTaskExecutor,
            @Qualifier("transactionAccStep")
            Step transactionAccStep,
            @Value("#{jobParameters['gridSize']}") int gridSize) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setTaskExecutor(threadPoolTaskExecutor);
        handler.setStep(transactionAccStep);
        handler.setGridSize(gridSize);
        return handler;
    }

    @Bean
    public Step transactionAccStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   ItemReader<TransactionLog> logReader,
                                   ItemWriter<TransactionLog> logAccumulator,
                                   StepExecutionListener stepExecutionListener,
                                   TaskExecutor threadPoolTaskExecutor) {
        return new StepBuilder("transactionAccStep", jobRepository)
                .<TransactionLog, TransactionLog>chunk(1000, transactionManager)
                .allowStartIfComplete(true)
                .listener(stepExecutionListener)
                .reader(logReader)
                .writer(logAccumulator)
                .taskExecutor(threadPoolTaskExecutor)
                .build();
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<TransactionLog> logReader(@Value("#{stepExecutionContext['file']}") File file,
                                                                  ObjectMapper objectMapper) {
        FlatFileItemReader<TransactionLog> logReader = new FlatFileItemReaderBuilder<TransactionLog>()
                .name("logReader")
                .resource(new FileSystemResource(file))
                .lineMapper((line, lineNumber) -> {
                            if (line.trim().isEmpty()) {
                                return null;
                            }
                            return objectMapper.readValue(line, TransactionLog.class);
                        }
                )
                .build();
        return new SynchronizedItemStreamReaderBuilder<TransactionLog>()
                .delegate(logReader)
                .build();
    }

    @Bean
    public ItemWriter<TransactionLog> logAccumulator(TransactionReportAccumulator accumulator) {
        return chunk -> chunk.getItems().forEach(accumulator::accumulate);
    }

    @Bean
    public Step transactionSaveStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    StepExecutionListener stepExecutionListener,
                                    ItemReader<TransactionReport> reportReader,
                                    ItemWriter<TransactionReport> reportWriter) {
        return new StepBuilder("transactionSaveStep", jobRepository)
                .<TransactionReport, TransactionReport>chunk(10, transactionManager)
                .allowStartIfComplete(true)
                .listener(stepExecutionListener)
                .reader(reportReader)
                .writer(reportWriter)
                .build();
    }

    @Bean
    @StepScope // transactionAccStep 이 후에 TransactionReportMemoryRepository 를 가져와야하기 떄문에 사용
    public IteratorItemReader<TransactionReport> reportReader(TransactionReportMemoryRepository repository) {
        return new IteratorItemReader<>(repository.getIterator());
    }

    @Bean
    public JdbcBatchItemWriter<TransactionReport> reportWriter(DataSource dataSource) {
        String sql = "INSERT INTO transaction_reports" +
                " (transaction_date, transaction_type, transaction_count, total_amount, customer_count, order_count, payment_method_kind_count, avg_product_count, total_product_quantity) " +
                "VALUES (:transactionDate, :transactionType, :transactionCount, :totalAmount, :customerCount, :orderCount, :paymentMethodKindCount, :avgProductCount, :totalProductQuantity)";
        return new JdbcBatchItemWriterBuilder<TransactionReport>()
                .dataSource(dataSource)
                .beanMapped()
                .sql(sql)
                .build();
    }

}
