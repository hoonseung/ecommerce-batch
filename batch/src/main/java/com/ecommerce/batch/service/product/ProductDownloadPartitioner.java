package com.ecommerce.batch.service.product;

import com.ecommerce.batch.domain.file.PartitionedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ProductDownloadPartitioner implements Partitioner {

    private final ProductService productService;
    private final PartitionedFileRepository partitionedFileRepository;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        List<String> ids = productService.getProductIds().stream().sorted().toList();
        Map<String, ExecutionContext> contextMap = new HashMap<>();

        int minIdx = 0;
        int maxIdx = ids.size() - 1;
        int partitionerNumber = 0;

        int targetSize = ((maxIdx - minIdx) / gridSize) + 1;
        int start = minIdx;
        int end = start + targetSize - 1;

        while (start <= maxIdx) {
            ExecutionContext context = new ExecutionContext();
            String partitioner = "partition" + partitionerNumber;
            contextMap.put(partitioner, context);


            if (end > maxIdx) {
                end = maxIdx;
            }
            context.putString("minId", ids.get(start));
            context.putString("maxId", ids.get(end));
            try {
                File file = partitionedFileRepository.putFile(partitioner, partitioner + "_", ".csv");
                context.put("file", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            start += targetSize;
            end += targetSize;
            partitionerNumber++;
        }

        return contextMap;
    }
}
