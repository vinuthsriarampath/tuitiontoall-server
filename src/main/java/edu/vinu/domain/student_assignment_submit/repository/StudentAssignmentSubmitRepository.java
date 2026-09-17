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

package edu.vinu.domain.student_assignment_submit.repository;

import edu.vinu.domain.student_assignment_submit.entity.StudentAssignmentSubmit;
import edu.vinu.domain.student_assignment_submit.repository.projections.AssignmentSubmissionDetailedProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAssignmentSubmitRepository extends JpaRepository<StudentAssignmentSubmit,Long> {
    @Query(value = """
    SELECT COUNT(sas.id)
        FROM student_assignment_submit sas
        WHERE sas.assignment_id = :assignmentId AND sas.student_id = :studentId
    """,nativeQuery = true)
    int countSubmitsByAssignmentAndStudent(Long assignmentId, Long studentId);


    @Query(value = """
    SELECT 
        sas.id AS submissionId,
        sas.student_id AS studentId,
        s.first_name AS firstName,
        s.last_name AS lastName,
        sas.assignment_id AS assignmentId,
        sas.url AS url,
        sas.grade AS grade,
        sas.status AS status,
        sas.marks_gained AS marksGained,
        sas.attempt_no AS attemptNo,
        sas.submitted_at AS submittedAt,
        sas.last_modified_date AS lastModifiedDate
    FROM student_assignment_submit sas
    INNER JOIN assignment a ON sas.assignment_id = a.id
    INNER JOIN student s ON sas.student_id = s.id
    WHERE sas.assignment_id = :assignmentId
    AND (:submissionId IS NULL OR sas.id = :submissionId)
    AND (:studentId IS NULL OR sas.student_id = :studentId)
    AND (:studentName IS NULL OR LOWER(CONCAT(s.first_name, ' ', s.last_name)) LIKE LOWER(CONCAT('%', :studentName, '%')))
    AND (:grade IS NULL OR sas.grade = :grade)
    AND (:status IS NULL OR sas.status = :status)
    AND (:attemptNo IS NULL OR sas.attempt_no = :attemptNo)
    AND (:marksGained IS NULL OR sas.marks_gained = :marksGained)
    AND (:minMarksGained IS NULL OR sas.marks_gained >= :minMarksGained)
    AND (:maxMarksGained IS NULL OR sas.marks_gained <= :maxMarksGained)
    """,nativeQuery = true)
    Page<AssignmentSubmissionDetailedProjection> getAllByAssignmentWithFilters(
            Long assignmentId,
            Long submissionId,
            Long studentId,
            String studentName,
            String grade,
            String status,
            Integer attemptNo,
            Integer marksGained,
            Integer minMarksGained,
            Integer maxMarksGained,
            Pageable pageable
    );
}
