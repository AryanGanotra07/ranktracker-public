package com.codingee.ranked.ranktracker.api.files;

import com.codingee.ranked.ranktracker.util.BaseResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @PostMapping("/upload")
    public BaseResponse uploadFile(@RequestParam("file") MultipartFile file) {
        List<String> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".csv")) {
                // Parse CSV file
                CSVParser csvParser = CSVFormat.DEFAULT.parse(reader);
                for (CSVRecord record : csvParser) {
                    String value = findFirstNonEmpty(record);
                    result.add(value);
                }
            } else {
                // Parse TXT file
                String line;
                while ((line = reader.readLine()) != null) {
                    result.add(line);
                }
            }

            return BaseResponse.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private String findFirstNonEmpty(CSVRecord record) {
        for (String value : record) {
            if (!value.isEmpty()) {
                return value;
            }
        }
        return ""; // Return an empty string if all columns are empty
    }
}
