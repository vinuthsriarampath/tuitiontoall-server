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

package edu.vinu.domain.lecture_record.service;

import edu.vinu.domain.lecture_record.request.LectureRecordDetailsUpdateRequest;
import edu.vinu.domain.lecture_record.request.LectureRecordUploadInitRequest;
import edu.vinu.domain.lecture_record.response.LectureRecordChunkUploadResponse;
import edu.vinu.domain.lecture_record.response.LectureRecordResponse;
import edu.vinu.domain.lecture_record.response.LectureRecordUploadInitResponse;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface LectureRecordService {
    LectureRecordUploadInitResponse initializeUpload(LectureRecordUploadInitRequest request);

    LectureRecordChunkUploadResponse uploadChunk(String uploadId, Integer chunkIndex, MultipartFile chunk);

    LectureRecordResponse completeUpload(String uploadId);

    ResponseEntity<ResourceRegion> streamVideo(String fileName, String rangeHeader);

    LectureRecordResponse updateLectureRecordDetails(Long id,LectureRecordDetailsUpdateRequest request);
}
