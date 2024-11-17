package com.ecommerce.batch.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SplitFilePartitioner implements Partitioner {

    private final List<File> fileList;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> contextMap = new HashMap<>();

        for (int i = 0; i < fileList.size(); i++) {
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.put("file", fileList.get(i));
            contextMap.put("partition" + i, executionContext);
        }
        return contextMap;
    }
}
