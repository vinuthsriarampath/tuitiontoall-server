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

package edu.vinu.repository;

import edu.vinu.domain.chapter.entity.ChapterEntity;
import edu.vinu.repository.projection.ChapterDetailedProjection;
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
}
