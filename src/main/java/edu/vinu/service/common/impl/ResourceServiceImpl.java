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

import edu.vinu.entity.ChapterEntity;
import edu.vinu.entity.ResourceUploadEntity;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.repository.ResourceRepository;
import edu.vinu.repository.ResourceUploadRepository;
import edu.vinu.request.resource.ResourceInitRequest;
import edu.vinu.response.resource.ResourceInitResponse;
import edu.vinu.service.common.ChapterService;
import edu.vinu.service.common.FileService;
import edu.vinu.service.common.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final ResourceUploadRepository resourceUploadRepository;
    private final ChapterService chapterService;
    private final FileService fileService;

    @Value("${file.chapter-resource.temp-path}")
    private String tempResourcePath;

    @Value("${file.chapter-resource.resource-path}")
    private String ResourcePath;

    @Override
    public ResourceInitResponse initializeUpload(ResourceInitRequest request) {
        if (alreadyExistsByNameAndChapterInResourceUpload(request.name(), request.chapterId()) && alreadyExistsByNameAndChapterInResource(request.name(), request.chapterId())) {
            throw new InvalidInputException("name","A resource with the same name already exists for this chapter.");
        }

        ChapterEntity chapterEntity = chapterService.getChapterEntityById(request.chapterId());
        String uploadId = UUID.randomUUID().toString();

        ResourceUploadEntity resourceUploadEntity = ResourceUploadEntity.builder()
                .uploadId(uploadId)
                .name(request.name())
                .chapter(chapterEntity)
                .originalFileName(request.originalFileName())
                .completed(false)
                .totalSize(request.totalSize())
                .totalChunks(request.totalChunks())
                .uploadedChunks(0)
                .build();

        ResourceUploadEntity save = resourceUploadRepository.save(resourceUploadEntity);

        String tempUploadPath = tempResourcePath + "/" + save.getUploadId();

        fileService.createDirectoryIfNotExists(tempUploadPath);

        return ResourceInitResponse.builder()
                .uploadId(save.getUploadId())
                .build();
    }

    private boolean alreadyExistsByNameAndChapterInResource(String name, Long chapterId){
        return resourceRepository.existsByNameAndChapterId(name, chapterId);
    }

    private boolean alreadyExistsByNameAndChapterInResourceUpload(String name, Long chapterId){
        return resourceRepository.existsByNameAndChapterId(name, chapterId);
    }
}
