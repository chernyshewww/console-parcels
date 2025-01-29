package com.hofftech.deliverysystem.service;

import com.hofftech.deliverysystem.exception.FileSaveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Service class for handling file operations, specifically saving content to a file.
 * This class provides a method to save content to a specified file.
 */
@Slf4j
@Service
public class FileService {

    /**
     * Saves the provided content to the specified file.
     *
     * @param fileName The name of the file where the content should be saved.
     * @param content The content to be written to the file.
     * @throws FileSaveException If an error occurs during the file writing process.
     */
    public void saveToFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            log.error("Error while saving the file: {}", fileName, e);
            throw new FileSaveException("Ошибка при сохранении файла: " + fileName, e);
        }
    }
}
