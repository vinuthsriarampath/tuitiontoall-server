/*
 * Copyright (c) 2026 vinuth sri arampath
 *
 * This code is the intellectual property of vinuth sri arampath and is protected under copyright law.
 * Unauthorized copying, modification, distribution, or use of this code, in whole or in part,
 * without prior written permission is strictly prohibited.
 *
 * Portions of this code may be generated with AI and modified by vinuth sri arampath
 * All rights reserved.
 *
 *
 */

package edu.vinu.service.common.impl;

import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.service.common.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public void saveFile(MultipartFile thumbnail, String filename,String savePath,StandardCopyOption copyOption) {
        try {
            Path filePath = this.createDirectoryIfNotExists(savePath).resolve(filename);
            Files.copy(thumbnail.getInputStream(), filePath, copyOption);
        } catch (IOException e) {
            throw new RuntimeException("Error saving thumbnail !");
        }
    }

    @Override
    public String extractFileExtension(String originalFileName) {
        String extension;
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            return extension;
        }
        throw new InvalidInputException("Invalid file name: " + originalFileName);
    }

    @Override
    public File loadFile(String filePath, String fileName) {
        Path resolvedfilePath = Paths.get(filePath).resolve(fileName);
        File file = resolvedfilePath.toFile();
        if(file.exists() || !file.isFile()){
            return file;
        }
        throw new NotFoundException("File not found: " + fileName);
    }

    @Override
    public byte[] fileToByteArray(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + file.getName());
        }
    }

    @Override
    public void deleteFilesMatchingPattern(Path directoryPath, String pattern) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, pattern)) {
            for (Path existingFile : stream) {
                Files.delete(existingFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting files matching pattern: " + pattern, e);
        }
    }

    @Override
    public Path createDirectoryIfNotExists(String savePath) {
        try {
            Path dir = Path.of(savePath);
            Files.createDirectories(dir);
            return dir;
        } catch (IOException e) {
            throw new RuntimeException("Error creating directory: " + savePath, e);
        }
    }


}
