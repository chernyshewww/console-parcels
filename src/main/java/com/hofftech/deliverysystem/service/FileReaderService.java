package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.exception.FileReaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileReaderService {

    public List<String> readFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            log.error("Error reading file '{}': {}", fileName, e.getMessage());
            throw new FileReaderException("Failed to read file: " + fileName, e);
        }
    }
}
