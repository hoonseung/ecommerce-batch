package com.ecommerce.batch.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

@ExtendWith(SpringExtension.class)
class FileUtilsTest {

    @Value("classpath:data/products_for_upload.csv")
    private Resource resource;

    @Test
    void splitCsvFiles() throws IOException {
        List<File> files = FileUtils.splitCSV(resource.getFile().getPath(), 2);

        Assertions.assertThat(files).hasSize(2);
        files.stream()
                .map(File::getName)
                .forEach(System.out::println);
    }
}