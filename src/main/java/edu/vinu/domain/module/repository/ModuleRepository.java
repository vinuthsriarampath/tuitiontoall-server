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

package edu.vinu.domain.module.repository;

import edu.vinu.domain.module.entity.ModuleEntity;
import edu.vinu.domain.module.repository.projection.DetailedModuleProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {
    boolean existsByNameAndBatchId(String name, Long batchId);

    @Query(
            value = """
                    SELECT *
                    FROM module m
                    WHERE(:status IS NULL OR m.status = :status)
                    AND (:batchId IS NULL OR m.batch_id = :batchId)
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM module m
                    WHERE(:status IS NULL OR m.status = :status)
                    AND (:batchId IS NULL OR m.batch_id = :batchId)
            """,
            nativeQuery = true
    )
    Page<ModuleEntity> getAllModules(Pageable pageable, @Param("status") String status, @Param("batchId") Long batchId);


    @Query(value = """
    SELECT
    m.id AS id,
    m.name AS name,
    m.status AS status,
    m.created_date AS createdDate,
    m.last_modified_date AS lastModifiedDate,
    
    b.id AS batchId,
    b.course_id AS courseId,
    b.name AS batchName,
    b.batch_status AS batchStatus,
    b.enrollment_status AS batchEnrollmentStatus,
    b.created_date AS batchCreatedDate,
    b.last_modified_date AS batchLastModifiedDate,
    
    t.id AS teacherId,
    t.first_name AS teacherFirstName,
    t.last_name AS teacherLastName,
    
    u.dp AS userDp,
    u.user_slug AS userSlug,
    u.email AS userEmail,
    u.contact AS userContact
    
    FROM module m
    JOIN batch b ON m.batch_id = b.id
    JOIN teacher t on m.teacher_id = t.id
    JOIN users u ON t.user_id = u.id
    WHERE m.id = :id
""",nativeQuery = true)
    Optional<DetailedModuleProjection> getDetailedModuleById(Long id);
}
