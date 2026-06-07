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

import edu.vinu.domain.batch.entity.BatchEntity;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.batch.dto.Batch;
import edu.vinu.request.BatchCreateRequest;
import edu.vinu.request.BatchUpdateRequest;

import java.util.List;

public interface BatchService {
    Batch createBatch(BatchCreateRequest request);
    Batch createBatch(CourseEntity course,BatchCreateRequest request);

    List<Batch> getAllBatchesByCourseId(Long courseId);

    Batch getBatchById(Long batchId);

    Batch updateBatchById(Long batchId, BatchUpdateRequest request);

    BatchEntity getBatchEntityById(Long batchId);

    Boolean isBatchOwner(BatchEntity batchEntity);

    Boolean isBatchBelongToCourse(BatchEntity batchEntity, Long courseId);
}
