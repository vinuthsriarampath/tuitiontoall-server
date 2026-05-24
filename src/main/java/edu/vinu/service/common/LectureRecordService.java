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

package edu.vinu.service.common;

import edu.vinu.request.lecture_record.LectureRecordDetailsUpdateRequest;
import edu.vinu.request.lecture_record.LectureRecordUploadInitRequest;
import edu.vinu.response.lecture_record.LectureRecordChunkUploadResponse;
import edu.vinu.response.lecture_record.LectureRecordResponse;
import edu.vinu.response.lecture_record.LectureRecordUploadInitResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LectureRecordService {
    LectureRecordUploadInitResponse initializeUpload(LectureRecordUploadInitRequest request);

    LectureRecordChunkUploadResponse uploadChunk(String uploadId, Integer chunkIndex, MultipartFile chunk);

    LectureRecordResponse completeUpload(String uploadId);

    ResponseEntity<Resource> streamVideo(String fileName, String rangeHeader) throws IOException;

    LectureRecordResponse updateLectureRecordDetails(Long id,LectureRecordDetailsUpdateRequest request);
}
