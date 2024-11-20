package com.ecommerce.batch.util;

import com.ecommerce.batch.dto.ProductDownloadCsvRow;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class FileUtils {

    public static List<File> splitCSV(String path, int fileCount) {
        long lineCount;
        try (Stream<String> lines = Files.lines(Path.of(path), UTF_8)) {
            lineCount = lines.count();
            long linesPerFile = (long) Math.ceil((double) lineCount / fileCount); // 5 / 2 = 3   3, 2   10 / 3 = 4, 4, 2
            return splitFiles(path, linesPerFile);
        } catch (IOException e) {
            log.error("e: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static List<File> splitFiles(String path, long linesPerFile) throws IOException {
        List<File> fileList = new ArrayList<>();
        BufferedWriter writer = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            boolean firstLine = true;
            boolean shouldFileCreate = true;
            int fileIndex = 0;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (shouldFileCreate) {
                    File splitFile = createTempFile("split_" + (fileIndex++) + "_", ".csv");
                    writer = new BufferedWriter(new FileWriter(splitFile));
                    fileList.add(splitFile);
                    shouldFileCreate = false;
                }

                writer.write(line);
                writer.newLine();
                lineCount++;

                if (lineCount >= linesPerFile) {
                    writer.close();
                    lineCount = 0;
                    shouldFileCreate = true;
                }
            }
        } finally {
            assert writer != null;
            writer.close();
        }
        return fileList;
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static void mergeFiles(List<File> files, String path) {
        String header = String.join(",", ReflectionUtils.getFieldNames(ProductDownloadCsvRow.class))
                + "\n";
        File newFile = new File(path);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(newFile))) {
            outputStream.write(header.getBytes(UTF_8));
            for (File file : files) {
                log.info("병합 중 파일 명: {}", file.getName());
                Files.copy(file.toPath(), outputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
