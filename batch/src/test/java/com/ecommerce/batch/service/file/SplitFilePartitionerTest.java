package com.ecommerce.batch.service.file;

import com.ecommerce.batch.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SplitFilePartitionerTest {

    @Test
    void givenFileList_whenPartitioning_thenReturnExecutionContextMap() throws IOException {
        List<File> fileList = createFiles(3);
        SplitFilePartitioner splitFilePartitioner = new SplitFilePartitioner(fileList);
        Map<String, ExecutionContext> contextMap = splitFilePartitioner.partition(3);

        assertThat(contextMap).hasSize(3);
        assertThat(contextMap).isEqualTo(Map.of(
                "partition0", new ExecutionContext(Map.of("file", fileList.get(0))),
                "partition1", new ExecutionContext(Map.of("file", fileList.get(1))),
                "partition2", new ExecutionContext(Map.of("file", fileList.get(2)))
        ));
    }

    @Test
    void givenEmptyFileList_thenReturnEmptyMap() {
        SplitFilePartitioner splitFilePartitioner = new SplitFilePartitioner(List.of());
        Map<String, ExecutionContext> result = splitFilePartitioner.partition(2);

        assertThat(result).isEmpty();
    }

    private static List<File> createFiles(int count) throws IOException {
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            fileList.add(FileUtils.createTempFile("test", ".tmp"));
        }
        return fileList;
    }
}