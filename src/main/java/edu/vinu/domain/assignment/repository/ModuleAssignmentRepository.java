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

package edu.vinu.domain.assignment.repository;

import edu.vinu.domain.assignment.entity.ModuleAssignmentEntity;
import edu.vinu.domain.assignment.repository.projection.ModuleAssignmentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ModuleAssignmentRepository extends JpaRepository<ModuleAssignmentEntity,Long> {
    boolean existsByAssignmentId(Long assignmentId);

    @Query(value = """
        SELECT
        ma.id AS id,
        ma.module_id AS moduleId,
        ma.assignment_id AS assignmentId,
        a.topic AS topic,
        a.description AS description,
        a.file_name AS fileName,
        a.total_marks AS totalMarks,
        a.available_on AS availableOn,
        a.due_date AS dueDate,
        a.late_submission AS lateSubmission,
        a.resubmission AS resubmission,
        a.max_attempts AS maxAttempts,
        a.created_date AS createdDate,
        a.last_modified_date AS lastModifiedDate
        FROM module_assignment ma
        JOIN assignment a ON a.id = ma.assignment_id
        WHERE ma.module_id = :moduleId
        AND (:assignmentId IS NULL OR ma.assignment_id = :assignmentId)
        AND (:topic IS NULL OR a.topic LIKE CONCAT('%',:topic,'%'))
        AND (:resubmission IS NULL OR a.resubmission = :resubmission)
        AND (:lateSubmission IS NULL OR a.late_submission = :lateSubmission)
        AND (:totalMarks IS NULL OR a.total_marks = :totalMarks)
        AND (:maxAttempts IS NULL OR a.max_attempts = :maxAttempts)
        AND (:availableOn IS NULL OR a.available_on = :availableOn)
        AND (:dueDate IS NULL OR a.due_date =:dueDate)
        AND (:createdDate IS NULL OR ma.created_date = :createdDate)
        AND (:lastModifiedDate IS NULL OR ma.last_modified_date = :lastModifiedDate)
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM module_assignment ma
        JOIN assignment a ON a.id = ma.assignment_id
        WHERE ma.module_id = :moduleId
        AND (:assignmentId IS NULL OR ma.assignment_id = :assignmentId)
        AND (:topic IS NULL OR a.topic LIKE CONCAT('%',:topic,'%'))
        AND (:resubmission IS NULL OR a.resubmission = :resubmission)
        AND (:lateSubmission IS NULL OR a.late_submission = :lateSubmission)
        AND (:totalMarks IS NULL OR a.total_marks = :totalMarks)
        AND (:maxAttempts IS NULL OR a.max_attempts = :maxAttempts)
        AND (:availableOn IS NULL OR a.available_on = :availableOn)
        AND (:dueDate IS NULL OR a.due_date =:dueDate)
        AND (:createdDate IS NULL OR ma.created_date = :createdDate)
        AND (:lastModifiedDate IS NULL OR ma.last_modified_date = :lastModifiedDate)
    """,
    nativeQuery = true)
    Page<ModuleAssignmentProjection> getAllModuleAssignmentByModule(Long moduleId, Long assignmentId, String topic, Boolean resubmission, Boolean lateSubmission, Integer totalMarks, Integer maxAttempts, LocalDateTime availableOn, LocalDateTime dueDate, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Pageable pageable);
}
