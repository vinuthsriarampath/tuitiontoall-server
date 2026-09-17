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
}
