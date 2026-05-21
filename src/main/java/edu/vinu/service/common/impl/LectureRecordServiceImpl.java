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
import edu.vinu.entity.LectureRecordEntity;
import edu.vinu.entity.LectureRecordUploadEntity;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.repository.LectureRecordRepository;
import edu.vinu.repository.LectureRecordUploadRepository;
import edu.vinu.request.lecture_record.LectureRecordUploadInitRequest;
import edu.vinu.response.lecture_record.LectureRecordChunkUploadResponse;
import edu.vinu.response.lecture_record.LectureRecordResponse;
import edu.vinu.response.lecture_record.LectureRecordUploadInitResponse;
import edu.vinu.service.common.ChapterService;
import edu.vinu.service.common.FileService;
import edu.vinu.service.common.LectureRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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
public class LectureRecordServiceImpl implements LectureRecordService {

    private final LectureRecordUploadRepository lectureRecordUploadRepository;
    private final LectureRecordRepository lectureRecordRepository;
    private final ChapterService chapterService;
    private final FileService fileService;
    private final Environment env;

    @Value("${file.lecture-record.temp-path}")
    private String tempLectureRecordPath;

    @Value("${file.lecture-record.video-path}")
    private String lectureRecordVideoPath;

    @Override
    public LectureRecordUploadInitResponse initializeUpload(LectureRecordUploadInitRequest request) {
        String uploadId = UUID.randomUUID().toString();

        LectureRecordUploadEntity uploadEntity =
                LectureRecordUploadEntity.builder()
                        .uploadId(uploadId)
                        .title(request.title())
                        .recordedDate(request.recordedDate())
                        .chapterId(request.chapterId())
                        .originalFileName(request.originalFileName())
                        .totalSize(request.totalSize())
                        .totalChunks(request.totalChunks())
                        .uploadedChunks(0)
                        .completed(false)
                        .build();

        lectureRecordUploadRepository.save(uploadEntity);

        String tempUploadPath =
                env.getProperty("file.lecture-record.temp-path") + "/" + uploadId;

        fileService.createDirectoryIfNotExists(tempUploadPath);

        return LectureRecordUploadInitResponse.builder()
                .uploadId(uploadId)
                .build();
    }

    @Override
    public LectureRecordChunkUploadResponse uploadChunk(String uploadId, Integer chunkIndex, MultipartFile chunk) {
        LectureRecordUploadEntity uploadEntity =
                lectureRecordUploadRepository.findById(uploadId)
                        .orElseThrow(() ->
                                new NotFoundException("Upload session not found")
                        );

        if (uploadEntity.getCompleted()) {
            throw new InvalidInputException(
                    "Upload already completed"
            );
        }

        String uploadDirectory =
                tempLectureRecordPath + "/" + uploadId;

        String chunkFileName =
                "chunk_" + chunkIndex + ".part";

        fileService.saveFile(
                chunk,
                chunkFileName,
                uploadDirectory,
                StandardCopyOption.REPLACE_EXISTING
        );

        uploadEntity.setUploadedChunks(
                uploadEntity.getUploadedChunks() + 1
        );

        lectureRecordUploadRepository.save(uploadEntity);

        return LectureRecordChunkUploadResponse.builder()
                .uploadedChunkIndex(chunkIndex)
                .build();
    }

    @Override
    public LectureRecordResponse completeUpload(String uploadId) {
        LectureRecordUploadEntity uploadEntity =
                lectureRecordUploadRepository.findById(uploadId)
                        .orElseThrow(() ->
                                new NotFoundException("Upload session not found")
                        );

        if (uploadEntity.getCompleted()) {
            throw new InvalidInputException(
                    "Upload already completed"
            );
        }

        if (!uploadEntity.getUploadedChunks()
                .equals(uploadEntity.getTotalChunks())) {

            throw new InvalidInputException(
                    "All chunks are not uploaded yet"
            );
        }

        String extension = fileService.extractFileExtension(
                uploadEntity.getOriginalFileName()
        );

        String finalVideoName =
                UUID.randomUUID() + extension;

        Path finalVideoPath =
                fileService.createDirectoryIfNotExists(
                        lectureRecordVideoPath
                ).resolve(finalVideoName);

        Path uploadDirectory =
                Path.of(
                        tempLectureRecordPath,
                        uploadId
                );

        try (OutputStream outputStream =
                     Files.newOutputStream(
                             finalVideoPath,
                             StandardOpenOption.CREATE,
                             StandardOpenOption.APPEND
                     )) {

            for (int i = 0; i < uploadEntity.getTotalChunks(); i++) {

                Path chunkPath =
                        uploadDirectory.resolve(
                                "chunk_" + i + ".part"
                        );

                if (!Files.exists(chunkPath)) {
                    throw new RuntimeException(
                            "Missing chunk: " + i
                    );
                }

                Files.copy(chunkPath, outputStream);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error merging chunks",
                    e
            );
        }

        ChapterEntity chapter = chapterService.getChapterEntityById(uploadEntity.getChapterId());

        LectureRecordEntity lectureRecord =
                LectureRecordEntity.builder()
                        .title(uploadEntity.getTitle())
                        .recordedDate(uploadEntity.getRecordedDate())
                        .url(finalVideoName)
                        .chapter(chapter)
                        .build();

        LectureRecordEntity savedLectureRecord = lectureRecordRepository.save(lectureRecord);

        uploadEntity.setCompleted(true);

        lectureRecordUploadRepository.save(uploadEntity);

        try {
            Files.walk(uploadDirectory)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error deleting temp chunks",
                    e
            );
        }

        return LectureRecordResponse.builder()
                .id(savedLectureRecord.getId())
                .title(savedLectureRecord.getTitle())
                .url(savedLectureRecord.getUrl())
                .chapterId(savedLectureRecord.getChapter().getId())
                .recordedDate(savedLectureRecord.getRecordedDate())
                .createdDate(savedLectureRecord.getCreatedDate())
                .lastModifiedDate(savedLectureRecord.getLastModifiedDate())
                .build();
    }
}
