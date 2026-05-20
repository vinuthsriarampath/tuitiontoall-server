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
import edu.vinu.repository.LectureRecordUploadRepository;
import edu.vinu.request.lecture_record.LectureRecordUploadInitRequest;
import edu.vinu.response.lecture_record.LectureRecordUploadInitResponse;
import edu.vinu.service.common.FileService;
import edu.vinu.service.common.LectureRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureRecordServiceImpl implements LectureRecordService {

    private final LectureRecordUploadRepository lectureRecordUploadRepository;
    private final FileService fileService;
    private final Environment env;

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
}
