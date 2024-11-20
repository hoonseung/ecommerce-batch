package com.ecommerce.batch.service.product;

import com.ecommerce.batch.domain.file.PartitionedFileRepository;
import com.ecommerce.batch.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ExecutionContext;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDownloadPartitionerTest {

    @Mock
    private ProductService productService;

    @Mock
    private PartitionedFileRepository partitionedFileRepository;

    @InjectMocks
    private ProductDownloadPartitioner productDownloadPartitioner;

    @Test
    void testMultipleProducts() {
        List<String> productIds = List.of("1", "2", "3", "4", "5");

        when(productService.getProductIds()).thenReturn(productIds);
        Map<String, ExecutionContext> result = productDownloadPartitioner.partition(2);

        assertAll(() -> assertThat(result).hasSize(2),
                () -> assertThat(result).isEqualTo(Map.of(
                                "partition0", new ExecutionContext(Map.of("minId", "1", "maxId", "3")),
                                "partition1", new ExecutionContext(Map.of("minId", "4", "maxId", "5"))
                        )
                ));

    }

    @Test
    void testFileRepository() throws IOException {
        String prefix = "partition";
        String suffix = ".csv";
        File expectedFile = FileUtils.createTempFile(prefix, suffix);
        List<String> productIds = List.of("1", "2", "3", "4", "5");
        
        when(productService.getProductIds()).thenReturn(productIds);
        when(partitionedFileRepository.putFile(anyString(), anyString(), anyString())).thenReturn(expectedFile);
        Map<String, ExecutionContext> result = productDownloadPartitioner.partition(2);

        assertAll(() -> assertThat(result).hasSize(2),
                () -> assertThat(result).isEqualTo(Map.of(
                        "partition0", new ExecutionContext(Map.of("minId", "1", "maxId", "3", "file", expectedFile)),
                        "partition1", new ExecutionContext(Map.of("minId", "4", "maxId", "5", "file", expectedFile))
                )),
                () -> verify(partitionedFileRepository, times(2)).putFile(anyString(), anyString(), anyString())
        );


    }
}