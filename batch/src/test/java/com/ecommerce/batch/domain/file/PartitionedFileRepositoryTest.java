package com.ecommerce.batch.domain.file;

import com.ecommerce.batch.util.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

class PartitionedFileRepositoryTest {


    @Test
    void getNotSortedFiles() throws IOException {
        File file1 = FileUtils.createTempFile("partition0", ".csv");
        File file2 = FileUtils.createTempFile("partition1", ".csv");
        File file3 = FileUtils.createTempFile("partition2", ".csv");

        Map<String, File> fileMap = Map.of("partition0", file1, "partition1", file2, "partition2", file3);

        fileMap.values()
                .stream()
                .map(String::valueOf)
                .forEach(System.out::println);

    }

    @Test
    void getSortedFiles() throws IOException {
        File file1 = FileUtils.createTempFile("partition0", ".csv");
        File file2 = FileUtils.createTempFile("partition1", ".csv");
        File file3 = FileUtils.createTempFile("partition2", ".csv");

        Map<String, File> fileMap = Map.of("partition0", file1, "partition1", file2, "partition2", file3);

        fileMap.values()
                .stream()
                .sorted(Comparator.comparing(File::getName))
                .map(String::valueOf)
                .forEach(System.out::println);
    }
}