package com.ecommerce.batch.domain.file;

import com.ecommerce.batch.util.FileUtils;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PartitionedFileRepository {

    private final Map<String, File> fileMap = new ConcurrentHashMap<>();

    public File putFile(String partition, String prefix, String suffix) throws IOException {
        File partitionedFile = FileUtils.createTempFile(prefix, suffix);
        fileMap.put(partition, partitionedFile);

        return partitionedFile;
    }

    public List<File> getFiles() {
        return fileMap.values().stream()
                .sorted(Comparator.comparing(File::getName))
                .toList();
    }
}
