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
import edu.vinu.entity.ResourceEntity;
import edu.vinu.entity.ResourceUploadEntity;
import edu.vinu.exception.custom.InternalServerErrorException;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.mapper.ResourceMapper;
import edu.vinu.repository.ResourceRepository;
import edu.vinu.repository.ResourceUploadRepository;
import edu.vinu.request.resource.ResourceInitRequest;
import edu.vinu.response.resource.ResourceChunkUploadResponse;
import edu.vinu.response.resource.ResourceInitResponse;
import edu.vinu.response.resource.ResourceResponse;
import edu.vinu.service.common.ChapterService;
import edu.vinu.service.common.FileService;
import edu.vinu.service.common.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
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

        String tempUploadPath = tempResourcePath + "/" + save.getUploadId();

        fileService.createDirectoryIfNotExists(tempUploadPath);

        return ResourceInitResponse.builder()
                .uploadId(save.getUploadId())
                .build();
    }

    @Override
    public ResourceChunkUploadResponse uploadChunk(String uploadId, Integer chunkIndex, MultipartFile file) {
        ResourceUploadEntity uploadEntity = getResourceUploadEntity(uploadId);

        if(uploadEntity.isCompleted()) throw new InvalidInputException("Upload has already been completed.");

        String directoryPath = tempResourcePath + "/" + uploadEntity.getUploadId();

        fileService.createDirectoryIfNotExists(directoryPath);

        String chunkFileName = "chunk_"+chunkIndex+".part";

        fileService.saveFile(file,chunkFileName,directoryPath, StandardCopyOption.REPLACE_EXISTING);

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

        String extension = fileService.extractFileExtension(uploadEntity.getOriginalFileName());

        String finalFileName = UUID.randomUUID() + extension;

        Path finalFilePath = fileService.createDirectoryIfNotExists(resourcePath).resolve(finalFileName);

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

        try {
            Files.walk(uploadDirectory)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting temp chunks", e);
        }

        return ResourceMapper.toResourceResponse(savedResourcesEntity);
    }

    @Override
    public ResponseEntity<ResourceRegion> viewResource(String fileName, String range) {
        Path path = getResourceFilePath(fileName);

        try {
            Resource resource = new UrlResource(path.toUri());

            Long contentLength = resource.contentLength();

            ResourceRegion region = getResourceRegion(resource,range,contentLength);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(
                            MediaTypeFactory
                                    .getMediaType(resource)
                                    .orElse(MediaType.APPLICATION_OCTET_STREAM)
                    )
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+fileName+"\"")
                    .body(region);
        } catch (IOException e) {
            throw new InternalServerErrorException("Error loading resource.");
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String fileName) {
        Path path = getResourceFilePath(fileName);

        try {
            Resource resource = new UrlResource(path.toUri());

            String contentType = Files.probeContentType(path);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\""
                    )
                    .contentLength(Files.size(path))
                    .body(resource);

        } catch (IOException e) {
            throw new InternalServerErrorException("Error loading resource.");
        }
    }

    private ResourceRegion getResourceRegion(Resource resource, String rangeHeader, Long contentLength) {
        if(rangeHeader == null){
            long rangeLength = Math.min(CHUNK_SIZE,contentLength);
            return new ResourceRegion(resource,0,rangeLength);
        }

        HttpRange httpRange = HttpRange
                .parseRanges(rangeHeader)
                .get(0);

        long start = httpRange.getRangeStart(contentLength);
        long end = httpRange.getRangeEnd(contentLength);

        long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);
        return new ResourceRegion(resource,start,rangeLength);
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
