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

package edu.vinu.service.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public interface FileService {
    void saveFile(MultipartFile thumbnail, String filename, String savePath, StandardCopyOption copyOption);
    String extractFileExtension(String originalFileName);
    File loadFile(String filePath,String fileName);
    byte[] fileToByteArray(File file);
    void deleteFilesMatchingPattern(Path directoryPath, String pattern);
    Path createDirectoryIfNotExists(String savePath);
}
