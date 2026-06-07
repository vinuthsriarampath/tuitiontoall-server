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

package edu.vinu.domain.profile_file.service.impl;

import edu.vinu.domain.profile_file.service.ProfileFileService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.user.repository.UserRepository;
import edu.vinu.infastructure.service.file_storage.FileService;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ProfileFileServiceImpl implements ProfileFileService {

    private final Environment env;
    private final UserService userService;
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Value("${file.profile.pic-path}")
    private String profilePicturePath;
    @Value("${file.profile.banner-path}")
    private String profileBannerPath;

    @Override
    public String uploadFile(MultipartFile file, String type) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!isValidFile(file)){
            throw new InvalidInputException("Invalid file type. Only JPEG, PNG, and GIF are allowed.");
        }

        Path directory = fileService.createDirectory(getProfileFilePath(type));

        fileService.deleteMatching(directory, userEntity.getUserSlug() + "-*");

        String fileName = generateUniqueProfileFilename(userEntity.getUserSlug(), file.getOriginalFilename());

        fileService.saveFile(file, directory, fileName, StandardCopyOption.REPLACE_EXISTING);

        switch (type) {
            case "dp" -> userEntity.setDp("/load/dp/" + fileName);
            case "banner" -> userEntity.setBanner("/load/banner/" + fileName);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        }

        UserEntity savedEntity = userRepository.save(userEntity);

        return switch (type) {
            case "dp" -> savedEntity.getDp();
            case "banner" -> savedEntity.getBanner();
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };

    }

    @Override
    public File loadFile(String type, String fileName) {
        return fileService.getFile(getProfileFilePath(type), fileName);
    }

    private Path getProfileFilePath(String type) {
        return switch (type) {
            case "dp" -> Path.of(profilePicturePath);
            case "banner" -> Path.of(profileBannerPath);
            default -> throw new IllegalArgumentException("Profile file type must be either 'dp' or 'banner'. Provided: " + type);
        };
    }

    private String generateUniqueProfileFilename(String userSlug, String originalFileName) {
        return String.format("%s-%s%s", userSlug, UUID.randomUUID(), fileService.extractExtension(originalFileName));
    }

    private Boolean isValidFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif") || contentType.equals("image/webp"));
    }
}
