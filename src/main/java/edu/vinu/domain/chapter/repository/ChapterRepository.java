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

package edu.vinu.domain.chapter.repository;

import edu.vinu.domain.chapter.entity.ChapterEntity;
import edu.vinu.domain.chapter.repository.projection.ChapterDetailedProjection;
import edu.vinu.domain.chapter.repository.projection.ChapterStatCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterEntity,Long> {
    boolean existsByModuleIdAndTitle(Long moduleId, String chapterName);

    int countByModuleId(Long moduleId);

    boolean existsByModuleIdAndChapterOrder(Long moduleId, int chapterOrder);

    List<ChapterEntity> findAllByModuleIdOrderByChapterOrderAsc(Long moduleId);

    @Query(value = """
    SELECT
    ch.id AS id,
    ch.title AS title,
    ch.status AS status,
    ch.chapter_order AS chapterOrder,
    ch.created_date AS createdDate,
    ch.last_modified_date AS lastModifiedDate,
    
    m.id AS ModuleId,
    m.name AS moduleName,
    m.status AS moduleStatus,
    m.teacher_id AS moduleTeacherId,
    m.batch_id AS moduleBatchId,
    m.created_date AS moduleCreatedDate,
    m.last_modified_date AS moduleLastModifiedDate
    FROM chapter ch
    JOIN module m ON ch.module_id = m.id
    WHERE ch.id = :id
""",nativeQuery = true)
    Optional<ChapterDetailedProjection> findDetailedById(Long id);

    @Query( value =
            """
    SELECT
    (SELECT COUNT(*)
        FROM lecture_recording lr
        WHERE lr.chapter_id = ch.id
    ) AS lectureRecordingCount,
    
    (SELECT COUNT(*)
        FROM chapter_assignment ca
        JOIN assignment a
        ON a.id = ca.assignment_id
        WHERE ca.chapter_id = ch.id
        AND a.available_on <= CURRENT_TIMESTAMP
    ) AS activeAssignmentsCount,
    
    (SELECT COUNT(*)
        FROM resource r
        WHERE r.chapter_id = ch.id
    ) AS resourceCount,
    
    (SELECT COUNT(*)
     FROM schedule_lecture sl
     WHERE sl.chapter_id = ch.id
     AND TIMESTAMP(sl.start_date, sl.end_time) >= CURRENT_TIMESTAMP
     AND sl.status IN ('SCHEDULED', 'LIVE')
    ) AS upcomingScheduleLectureCount
    
    FROM chapter ch
    WHERE ch.id = :chapterId
    """,
    nativeQuery = true)
    ChapterStatCountProjection getStatCountByChapterId(Long chapterId);
}
