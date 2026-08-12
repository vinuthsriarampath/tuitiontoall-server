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

package edu.vinu.domain.student_batch_enrollment.repository;

import edu.vinu.domain.student_batch_enrollment.entity.StudentBatchEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentBatchEnrollmentRepository extends JpaRepository<StudentBatchEnrollment,Long> {
    @Query(value = """ 
    SELECT COUNT(*)
    FROM student_batch_enrollment sbe
    WHERE sbe.student_id = :studentId
    AND sbe.batch_id = :batchId
    """, nativeQuery = true)
    long countByStudentIdAndBatchId(@Param("studentId") Long studentId, @Param("batchId") Long batchId);

    @Query(value = """
    SELECT COUNT(*)
    FROM student_batch_enrollment sbe
    INNER JOIN batch b
    ON b.id = sbe.batch_id
    WHERE sbe.student_id = :studentId
    AND b.course_id = :courseId
    AND b.batch_status <> 'COMPLETED'
    """, nativeQuery = true)
    long countActiveEnrollmentByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query(value = """
    SELECT COUNT(*)
    FROM student_batch_enrollment sbe
    WHERE sbe.batch_id = :batchId
    """, nativeQuery = true)
    long countEnrollmentsByBatchId(@Param("batchId") Long batchId);
}
