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

package edu.vinu.domain.resource.service.impl;

import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.chapter.entity.ChapterEntity;
import edu.vinu.domain.chapter.service.ChapterService;
import edu.vinu.domain.resource.entity.ResourceEntity;
import edu.vinu.domain.resource.entity.ResourceUploadEntity;
import edu.vinu.domain.resource.mapper.ResourceMapper;
import edu.vinu.domain.resource.repository.ResourceRepository;
import edu.vinu.domain.resource.repository.ResourceUploadRepository;
import edu.vinu.domain.resource.request.ResourceInitRequest;
import edu.vinu.domain.resource.response.ResourceChunkUploadResponse;
import edu.vinu.domain.resource.response.ResourceInitResponse;
import edu.vinu.domain.resource.response.ResourceResponse;
import edu.vinu.domain.resource.service.ResourceService;
import edu.vinu.infastructure.service.file_storage.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
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
    private String resourcePath;

    private static final long CHUNK_SIZE = 1024 * 1024;

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

        fileService.createDirectory(Path.of(tempResourcePath,uploadId));

        return ResourceInitResponse.builder()
                .uploadId(save.getUploadId())
                .build();
    }

    @Override
    public ResourceChunkUploadResponse uploadChunk(String uploadId, Integer chunkIndex, MultipartFile file) {
        ResourceUploadEntity uploadEntity = getResourceUploadEntity(uploadId);

        if(uploadEntity.isCompleted()) throw new InvalidInputException("Upload has already been completed.");

        Path directoryPath = Path.of(tempResourcePath,uploadEntity.getUploadId());

        String chunkFileName = "chunk_"+chunkIndex+".part";

        fileService.saveFile(file, directoryPath, chunkFileName, StandardCopyOption.REPLACE_EXISTING);

        uploadEntity.setUploadedChunks(uploadEntity.getUploadedChunks() + 1);

        resourceUploadRepository.save(uploadEntity);

        return ResourceChunkUploadResponse.builder()
                .uploadedChunkIndex(chunkIndex)
                .build();
    }

    @Override
    public ResourceResponse completeUpload(String uploadId) {
        ResourceUploadEntity uploadEntity = getResourceUploadEntity(uploadId);

        if(uploadEntity.isCompleted()) throw new InvalidInputException("Upload has already been completed.");

        if(!uploadEntity.getUploadedChunks().equals(uploadEntity.getTotalChunks())) throw new InvalidInputException("All the chunks haven't uploaded!");

        String extension = fileService.extractExtension(uploadEntity.getOriginalFileName());

        String finalFileName = UUID.randomUUID() + extension;

        Path finalFilePath = fileService.createDirectory(Path.of(resourcePath)).resolve(finalFileName);

        Path uploadDirectory = Path.of(tempResourcePath, uploadId);

        try (OutputStream outputStream = Files.newOutputStream(finalFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            for (int i = 0; i < uploadEntity.getTotalChunks(); i++) {

                Path chunkPath = uploadDirectory.resolve("chunk_" + i + ".part");

                if (!Files.exists(chunkPath)) throw new RuntimeException("Missing chunk: " + i);

                Files.copy(chunkPath, outputStream);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error merging chunks", e);
        }

        ChapterEntity chapterEntity = chapterService.getChapterEntityById(uploadEntity.getChapter().getId());

        ResourceEntity resourceEntity = ResourceEntity.builder()
                .name(uploadEntity.getName())
                .fileName(finalFileName)
                .chapter(chapterEntity)
                .build();
        ResourceEntity savedResourcesEntity = resourceRepository.save(resourceEntity);

        uploadEntity.setCompleted(true);
        uploadEntity.setResource(savedResourcesEntity);

        resourceUploadRepository.save(uploadEntity);

        fileService.deleteDirectory(uploadDirectory);

        return ResourceMapper.toResourceResponse(savedResourcesEntity);
    }

    @Override
    public ResponseEntity<ResourceRegion> viewResource(String fileName, String range) {

        Path path = fileService.resolve(Path.of(resourcePath), fileName);

        Resource resource = fileService.getResource(Path.of(resourcePath), fileName);

        long fileSize = fileService.size(path);

        MediaType mediaType = MediaType.parseMediaType(fileService.detectContentType(path));

        ResourceRegion region = fileService.getRegion(resource, range, fileSize, CHUNK_SIZE);

        return fileService.buildPartialResponse(resource, region, mediaType, fileName, fileSize);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String fileName) {

        Path path = getResourceFilePath(fileName);

        Resource resource = fileService.getResource(Path.of(resourcePath), fileName);

        MediaType mediaType = MediaType.parseMediaType(fileService.detectContentType(path));

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\""
                )
                .contentLength(fileService.size(path))
                .body(resource);
    }

    private Path getResourceFilePath(String fileName) {
        Path path = Path.of(resourcePath,fileName);
        if(!Files.exists(path)) throw new NotFoundException("Resource not found.");
        return path;
    }

    private ResourceUploadEntity getResourceUploadEntity(String uploadId) {
        return resourceUploadRepository.findById(uploadId).orElseThrow(()-> new NotFoundException("Resource upload initialization not found."));
    }

    private boolean alreadyExistsByNameAndChapterInResource(String name, Long chapterId){
        return resourceRepository.existsByNameAndChapterId(name, chapterId);
    }

    private boolean alreadyExistsByNameAndChapterInResourceUpload(String name, Long chapterId){
        return resourceUploadRepository.existsByNameAndChapterId(name, chapterId);
    }
}
