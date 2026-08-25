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

import edu.vinu.domain.reporting.projection.TrendPointProjection;
import edu.vinu.domain.student.repository.projection.StudentUserProjection;
import edu.vinu.domain.student_batch_enrollment.entity.StudentBatchEnrollment;
import edu.vinu.domain.student_batch_enrollment.repository.projection.EnrollmentDistributionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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


    @Query(value = """
    SELECT
        u.id AS userId,
        u.email AS email,
        u.contact AS contact,
        u.dp AS dp,
        u.address AS address,
        s.id AS studentId,
        s.first_name AS firstName,
        s.last_name AS lastName,
        s.dob AS dob
    FROM student_batch_enrollment sbe
    INNER JOIN student s ON s.id = sbe.student_id
    INNER JOIN users u ON u.id = s.user_id
    WHERE sbe.batch_id = :batchId
    """,nativeQuery = true)
    Page<StudentUserProjection> findAllStudentsByBatchId(Long batchId, Pageable pageable);

    @Query(value = """
    SELECT EXISTS (
        SELECT 1
        FROM student_batch_enrollment sbe
        INNER JOIN batch b ON b.id = sbe.batch_id
        WHERE sbe.student_id = :studentId
            AND b.course_id = :courseId
    )
    """, nativeQuery = true)
    int existsEnrollmentByStudentAndCourse(Long studentId, Long courseId);

    @Transactional
    @Modifying
    @Query(value = """
    UPDATE student_batch_enrollment sbe
        INNER JOIN batch b ON b.id = sbe.batch_id
        SET sbe.status = 'COMPLETED'
        WHERE b.batch_status = 'COMPLETED'
            AND sbe.status <> 'COMPLETED'
    """,nativeQuery = true)
    int completeEnrollmentsWhereBatchCompleted();

    @Query(value = """
    SELECT COUNT(DISTINCT sbe.student_id)
        FROM student_batch_enrollment sbe
            INNER JOIN batch b ON b.id = sbe.batch_id
        WHERE b.course_id = :courseId
            AND b.batch_status = :batchStatus
            AND sbe.status = :enrollmentStatus
    """, nativeQuery = true)
    Long countActiveStudentsByCourseId(Long courseId, String batchStatus, String enrollmentStatus);

    @Query(value = """
    SELECT COUNT(DISTINCT sbe.student_id)
        FROM student_batch_enrollment sbe
            INNER JOIN batch b ON b.id = sbe.batch_id
            INNER JOIN courses c ON c.id = b.course_id
        WHERE c.institute_id = :instituteId
            AND (:enrollmentStatus IS NULL OR sbe.status = :enrollmentStatus)
    """, nativeQuery = true)
    Long countEnrollmentsByInstitute(Long instituteId, String enrollmentStatus);

    @Query(value = """
    SELECT COUNT(DISTINCT sbe.student_id)
        FROM student_batch_enrollment sbe
        INNER JOIN batch b ON b.id = sbe.batch_id
        INNER JOIN courses c ON c.id = b.course_id
        INNER JOIN institute i ON i.id = c.institute_id
        WHERE c.institute_id = :instituteId
            AND sbe.created_date >= :startDateTime
            AND sbe.created_date < :endDateTime
    """, nativeQuery = true)
    Long countStudentsEnrolledBetween(Long instituteId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
        DATE_FORMAT(sbe.created_date, '%Y-%m-%d %H:00:00') AS bucket,
        COUNT(DISTINCT sbe.student_id) AS value
    FROM student_batch_enrollment sbe
    INNER JOIN batch b ON b.id = sbe.batch_id
    INNER JOIN courses c ON c.id = b.course_id
    WHERE c.institute_id = :instituteId
        AND sbe.created_date >= :startDateTime
        AND sbe.created_date < :endDateTime
    GROUP BY DATE_FORMAT(sbe.created_date, '%Y-%m-%d %H')
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getHourlyStudentEnrollmentTrend(Long instituteId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
        DATE_FORMAT(sbe.created_date, '%Y-%m-%d 00:00:00') AS bucket,
        COUNT(DISTINCT sbe.student_id) AS value
    FROM student_batch_enrollment sbe
    INNER JOIN batch b ON b.id = sbe.batch_id
    INNER JOIN courses c ON c.id = b.course_id
    WHERE c.institute_id = :instituteId
        AND sbe.created_date >= :startDateTime
        AND sbe.created_date < :endDateTime
    GROUP BY DATE(sbe.created_date)
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getDailyStudentEnrollmentTrend(Long instituteId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
        DATE_FORMAT(sbe.created_date, '%Y-%m-01 00:00:00') AS bucket,
        COUNT(DISTINCT sbe.student_id) AS value
    FROM student_batch_enrollment sbe
    INNER JOIN batch b ON b.id = sbe.batch_id
    INNER JOIN courses c ON c.id = b.course_id
    WHERE c.institute_id = :instituteId
        AND sbe.created_date >= :startDateTime
        AND sbe.created_date < :endDateTime
    GROUP BY YEAR(sbe.created_date), MONTH(sbe.created_date)
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getMonthlyStudentEnrollmentTrend(Long instituteId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
    COUNT(*) AS totalEnrollments,
    COUNT(CASE WHEN sbe.status = 'ACTIVE' THEN 1 END) AS activeEnrollments,
    COUNT(CASE WHEN sbe.status = 'COMPLETED' THEN 1 END) AS completedEnrollments,
    COUNT(CASE WHEN sbe.status = 'SUSPENDED' THEN 1 END) AS suspendedEnrollments
    FROM student_batch_enrollment sbe
    INNER JOIN batch b ON b.id = sbe.batch_id
    INNER JOIN courses c ON c.id = b.course_id
    WHERE c.institute_id = :instituteId
    """, nativeQuery = true)
    EnrollmentDistributionProjection getEnrollmentDistributionByInstituteId(Long instituteId);
}
