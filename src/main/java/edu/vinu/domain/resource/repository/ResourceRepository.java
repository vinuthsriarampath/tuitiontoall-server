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

package edu.vinu.domain.resource.repository;

import edu.vinu.domain.resource.entity.ResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity,Long> {
    boolean existsByNameAndChapterId(String name, Long chapterId);

    @Query(value = """
        SELECT
            r.id,
            r.chapter_id,
            r.name,
            r.file_name,
            r.created_date,
            r.last_modified_date
        FROM resource r
        WHERE (:chapterId IS NULL OR r.chapter_id = :chapterId)
            AND (:resourceId IS NULL OR r.id = :resourceId)
            AND (:name IS NULL OR r.name LIKE CONCAT('%', :name, '%'))
    """,
   countQuery = """
        SELECT COUNT(*)
        FROM resource r
        WHERE (:chapterId IS NULL OR r.chapter_id = :chapterId)
            AND (:resourceId IS NULL OR r.id = :resourceId)
            AND (:name IS NULL OR r.name LIKE CONCAT('%', :name, '%'))
   """,
   nativeQuery=true)
   Page<ResourceEntity> getAllResourcesByChapter(Long chapterId, Long resourceId, String name, Pageable pageable);
}
