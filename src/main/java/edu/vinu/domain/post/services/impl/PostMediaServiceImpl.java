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

package edu.vinu.domain.post.services.impl;

import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.post.dto.response.PostMediaResponse;
import edu.vinu.domain.post.mapper.PostMediaMapper;
import edu.vinu.domain.post.repository.PostMediaRepository;
import edu.vinu.domain.post.services.PostMediaService;
import edu.vinu.infastructure.service.file_storage.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostMediaServiceImpl implements PostMediaService {

    private final PostMediaRepository postMediaRepository;
    private final FileService fileService;

    @Value("${file.post-path}")
    private String postPath;

    @Override
    public List<PostMediaResponse> getPostMediaByPostId(Long postId) {
        return postMediaRepository.findAllByPostId(postId).stream()
                .map(PostMediaMapper::toPostMediaResponse)
                .toList();
    }

    @Override
    public ResponseEntity<?> loadMediaResource(String fileName, String rangeHeader) {
        Path filePath = Path.of(postPath, fileName);

        if (!fileService.exists(filePath)) {
            throw new NotFoundException("Media file not found: " + fileName);
        }

        Resource resource = fileService.getResource(Path.of(postPath), fileName);
        String contentType = fileService.detectContentType(filePath);
        MediaType mediaType = MediaType.parseMediaType(contentType);
        long fileSize = fileService.size(filePath);

        // 1. If video / audio and client sends Range header -> Serve HTTP 206 Partial Content
        if (rangeHeader != null && contentType.startsWith("video/")) {
            long chunkSize = 1024 * 1024; // 1 MB buffer chunk for smooth streaming
            ResourceRegion region = fileService.getRegion(resource, rangeHeader, fileSize, chunkSize);
            return fileService.buildPartialResponse(resource, region, mediaType, fileName, fileSize);
        }

        // 2. Full Content Delivery (HTTP 200) for Images, Documents, or non-ranged video requests
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize))
                .body(resource);
    }

}
