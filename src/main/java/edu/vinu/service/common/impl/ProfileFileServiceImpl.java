/*
 * Copyright (c) 2025 vinuth sri arampath
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

import edu.vinu.model.user_models.User;
import edu.vinu.service.common.ProfileFileService;
import edu.vinu.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ProfileFileServiceImpl implements ProfileFileService {

    private final Environment env;
    private final UserService userService;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {

            User user =userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

            Path dir = Paths.get("uploads\\profiles\\"+folder);
            Files.createDirectories(dir);

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            String baseFilename = user.getUserSlug();

            // Delete any existing file with the same base name (regardless of extension)
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, baseFilename + ".*")) {
                for (Path existingFile : stream) {
                    Files.delete(existingFile);
                }
            }

            String filename = user.getUserSlug() + extension;
            Path filePath = dir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Resource loadFile(String type, String fileName) {
        try {
            Path filePath = Paths.get(getPath(type)).resolve(fileName);
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPath(String type) {
        return switch (type) {
            case "dp" -> env.getProperty("file.profile.pic-path");
            case "banner" -> env.getProperty("file.profile.banner-path");
            default -> throw new IllegalArgumentException("Invalid type");
        };
    }
}
