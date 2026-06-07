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

package edu.vinu.infastructure.service.file_storage.impl;

import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.infastructure.service.file_storage.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Comparator;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public void saveFile(MultipartFile file, Path directory, String fileName, StandardCopyOption option) {
        try {
            Path path = this.createDirectory(directory).resolve(fileName);
            Files.copy(file.getInputStream(), path, option);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    @Override
    public Path saveAndReturnPath(MultipartFile file, Path directory, String fileName, StandardCopyOption option) {
        try {
            Path path = this.createDirectory(directory).resolve(fileName);
            Files.copy(file.getInputStream(), path, option);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Error saving file", e);
        }
    }

    @Override
    public File getFile(Path directory, String fileName) {
        Path path = directory.resolve(fileName);
        File file = path.toFile();
        if (!file.exists() || !file.isFile()) {
            throw new NotFoundException("File not found: " + fileName);
        }
        return file;
    }

    @Override
    public Resource getResource(Path directory, String fileName) {
        try {
            Path path = directory.resolve(fileName);
            if (!Files.exists(path)) {
                throw new NotFoundException("File not found: " + fileName);
            }
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error loading file resource", e);
        }
    }

    @Override
    public byte[] getBytes(Path filePath) {
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }

    @Override
    public ResourceRegion getRegion(Resource resource, String rangeHeader, long fileSize, long chunkSize) {
        try {
            if (rangeHeader == null) {
                long length = Math.min(chunkSize, fileSize);
                return new ResourceRegion(resource, 0, length);
            }

            HttpRange range = HttpRange.parseRanges(rangeHeader).get(0);

            long start = range.getRangeStart(fileSize);
            long end = range.getRangeEnd(fileSize);

            long length = Math.min(chunkSize, end - start + 1);

            return new ResourceRegion(resource, start, length);

        } catch (Exception e) {
            throw new RuntimeException("Error calculating range", e);
        }
    }

    @Override
    public ResponseEntity<ResourceRegion> buildPartialResponse(Resource resource, ResourceRegion region, MediaType mediaType, String fileName, long fileSize) {
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(mediaType)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize))
                .body(region);
    }

    @Override
    public void delete(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file", e);
        }
    }

    @Override
    public void deleteDirectory(Path directory) {
        try {
            Files.walk(directory)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting directory", e);
        }
    }

    @Override
    public String extractExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        throw new InvalidInputException("Invalid file name: " + fileName);
    }


    @Override
    public void deleteMatching(Path directory, String pattern) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, pattern)) {
            for (Path existingFile : stream) {
                Files.delete(existingFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting files matching pattern: " + pattern, e);
        }
    }

    @Override
    public long size(Path filePath) {
        try {
            return Files.size(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error getting file size", e);
        }
    }

    @Override
    public boolean exists(Path filePath) {
        return Files.exists(filePath);
    }

    @Override
    public String detectContentType(Path filePath) {
        try {
            String type = Files.probeContentType(filePath);
            return type != null ? type : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    @Override
    public Path createDirectory(Path path) {
        try {
            Files.createDirectories(path);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Error creating directory: " + path, e);
        }
    }

    @Override
    public Path resolve(Path directory, String fileName) {
        return directory.resolve(fileName);
    }

}
