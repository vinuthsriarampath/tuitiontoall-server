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

import edu.vinu.entity.LectureRecordUploadEntity;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.repository.LectureRecordUploadRepository;
import edu.vinu.request.lecture_record.LectureRecordUploadInitRequest;
import edu.vinu.response.lecture_record.LectureRecordChunkUploadResponse;
import edu.vinu.response.lecture_record.LectureRecordResponse;
import edu.vinu.response.lecture_record.LectureRecordUploadInitResponse;
import edu.vinu.service.common.FileService;
import edu.vinu.service.common.LectureRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureRecordServiceImpl implements LectureRecordService {

    private final LectureRecordUploadRepository lectureRecordUploadRepository;
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
}
