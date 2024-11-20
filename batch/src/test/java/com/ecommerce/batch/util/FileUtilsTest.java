package com.ecommerce.batch.util;

import com.ecommerce.batch.dto.ProductDownloadCsvRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class FileUtilsTest {

    @Value("classpath:data/products_for_upload.csv")
    private Resource resource;
    @TempDir
    Path tempDir;

    @Test
    void splitCsvFiles() throws IOException {
        List<File> files = FileUtils.splitCSV(resource.getFile().getPath(), 2);

        assertThat(files).hasSize(2);
        files.stream()
                .map(File::getName)
                .forEach(System.out::println);
    }

    @Test
    void createTempFile() throws IOException {
        File tempFile = File.createTempFile("product", ".csv");

        assertThat(tempFile.exists()).isTrue();
    }

    @Test
    void mergeFile() throws IOException {
        File file1 = createFile("filename1.csv", "Content1\n");
        File file2 = createFile("filename2.csv", "Content2\n");
        File file3 = createFile("filename3.csv", "Content3");

        File outputFile = new File(tempDir.toFile(), "output.txt");

        FileUtils.mergeFiles(List.of(file1, file2, file3), outputFile.getPath());
        String header = String.join(",", ReflectionUtils.getFieldNames(ProductDownloadCsvRow.class));

        assertThat(Files.readAllLines(outputFile.toPath())).containsExactly(header, "Content1", "Content2", "Content3");
    }

    private File createFile(String fileName, String content) throws IOException {
        File file = new File(tempDir.toFile(), fileName);
        Files.writeString(file.toPath(), content);
        return file;
    }
}