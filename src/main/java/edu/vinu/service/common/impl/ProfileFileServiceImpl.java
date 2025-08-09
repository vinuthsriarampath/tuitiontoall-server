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

import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.exception.custom.UserNotFoundException;
import edu.vinu.model.user_models.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.service.common.ProfileFileService;
import edu.vinu.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Service
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ProfileFileServiceImpl implements ProfileFileService {

    private final Environment env;
    private final UserService userService;
    private final ModelMapper mapper;
    private final UserRepository userRepository;

    @Override
    public User uploadFile(MultipartFile file, String type) {
        try {

            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = Optional.ofNullable(userRepository.findByEmail(email))
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Path dir = Paths.get("uploads\\profiles\\"+type);
            Files.createDirectories(dir);

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }

            String baseFilename = userEntity.getUserSlug();

            // Delete any existing file with the same base name (regardless of extension)
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, baseFilename + ".*")) {
                for (Path existingFile : stream) {
                    Files.delete(existingFile);
                }
            }

            String filename = userEntity.getUserSlug() + extension;
            Path filePath = dir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            switch (type){
                case "dp" -> userEntity.setDp("/api/v2/profile-files/load/dp/"+filename);
                case "banner" -> userEntity.setBanner("/api/v2/profile-files/load/banner/"+filename);
                default -> throw new IllegalArgumentException("Invalid type: " + type);
            }
            UserEntity savedEntity=userRepository.save(userEntity);
            return mapper.map(savedEntity, User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public File loadFile(String type, String fileName) {
        Path filePath = Paths.get(getPath(type)).resolve(fileName);
        return filePath.toFile();
    }

    private String getPath(String type) {
        return switch (type) {
            case "dp" -> env.getProperty("file.profile.pic-path");
            case "banner" -> env.getProperty("file.profile.banner-path");
            default -> throw new IllegalArgumentException("Invalid type");
        };
    }
}
