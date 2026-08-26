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

package edu.vinu.domain.batch.repository;

import edu.vinu.domain.batch.entity.BatchEntity;
import edu.vinu.domain.batch.repository.projection.BatchDetailedProjection;
import edu.vinu.domain.batch.repository.projection.BatchProjection;
import edu.vinu.domain.reporting.projection.TrendPointProjection;
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
public interface BatchRepository extends JpaRepository<BatchEntity, Long> {
    Boolean existsByNameAndCourseId(String name, Long courseId);
    @Query(value = """
            SELECT *
            FROM batch b
            WHERE b.course_id = :courseId
    """,nativeQuery = true)
    List<BatchEntity> getAllBatchesByCourseId(@Param("courseId") Long courseId);


    @Modifying
    @Transactional
    @Query(value = """
    UPDATE batch
    SET enrollment_status = :closeEnrollmentStatus, batch_status = :ongoingBatchStatus
    WHERE TIMESTAMP(start_date,start_time) <= CURRENT_TIMESTAMP
    AND batch_status = :preparationStatus
    and enrollment_status = :openEnrollmentStatus;
    """, nativeQuery = true)
    int closeEnrollmentAndStartPreparationBatch(
            @Param("openEnrollmentStatus") String openEnrollmentStatus,
            @Param("closeEnrollmentStatus") String closeEnrollmentStatus,
            @Param("preparationStatus") String preparationStatus,
            @Param("ongoingBatchStatus") String ongoingBatchStatus
    );

    @Query(value = """
SELECT
    b.id AS id,
    b.course_id AS courseId,
    b.name AS name,
    b.is_seat_limited AS isSeatLimited,
    b.max_seat_limit AS maxSeatLimit,
    b.start_date AS startDate,
    b.start_time AS startTime,
    b.batch_status AS batchStatus,
    b.enrollment_status AS enrollmentStatus,
    b.created_date AS createdDate,
    b.last_modified_date AS lastModifiedDate
FROM batch b
WHERE b.course_id = :courseId
    AND b.enrollment_status = 'OPEN'
    AND b.batch_status != 'COMPLETED'
ORDER BY b.start_date DESC
""", nativeQuery = true)
    List<BatchProjection> getAllEnrollableBatchesByCourse(Long courseId);


    @Query(value = """
    SELECT COUNT(*)
    FROM batch b
    WHERE b.course_id = :courseId
    AND b.batch_status = :batchStatus
    """, nativeQuery = true)
    Long countByCourseIdAndBatchStatus(Long courseId, String batchStatus);


    @Query(value = """
    SELECT COUNT(*)
    FROM batch b
    JOIN courses c ON b.course_id = c.id
    WHERE c.institute_id = :instituteId
    AND b.batch_status = :batchStatus
    """,nativeQuery = true)
    Long countBatchesByInstituteIdAndBatchStatus(Long instituteId, String batchStatus);

    @Query(value = """
    SELECT COUNT(*)
    FROM batch b
    JOIN courses c ON b.course_id = c.id
    WHERE c.institute_id = :instituteId
    AND b.batch_status = :batchStatus
    AND b.created_date >= :startDateTime
    AND b.created_date < :endDateTime
    """,nativeQuery = true)
    Long countBatchesByInstituteIdAndBatchStatusBetween(Long instituteId, String batchStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
    DATE_FORMAT(b.created_date, '%Y-%m-%d %H:00:00') AS bucket,
    COUNT(*) AS value
    FROM batch b
    JOIN courses c ON b.course_id = c.id
    WHERE c.institute_id = :instituteId
    AND b.batch_status = :batchStatus
    AND b.created_date >= :startDateTime
    AND b.created_date < :endDateTime
    GROUP BY DATE_FORMAT(b.created_date, '%Y-%m-%d %H')
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getHourlyBatchesTrendByInstituteAndBatchStatus(Long instituteId, String batchStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
    DATE_FORMAT(b.created_date, '%Y-%m-%d 00:00:00') AS bucket,
    COUNT(*) AS value
    FROM batch b
    JOIN courses c ON b.course_id = c.id
    WHERE c.institute_id = :instituteId
    AND b.batch_status = :batchStatus
    AND b.created_date >= :startDateTime
    AND b.created_date < :endDateTime
    GROUP BY DATE(b.created_date)
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getDailyBatchesTrendByInstituteAndBatchStatus(Long instituteId, String batchStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
    DATE_FORMAT(b.created_date, '%Y-%m-01 00:00:00') AS bucket,
    COUNT(*) AS value
    FROM batch b
    JOIN courses c ON b.course_id = c.id
    WHERE c.institute_id = :instituteId
    AND b.batch_status = :batchStatus
    AND b.created_date >= :startDateTime
    AND b.created_date < :endDateTime
    GROUP BY YEAR(b.created_date), MONTH(b.created_date)
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getMonthlyBatchesTrendByInstituteAndBatchStatus(Long instituteId, String batchStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value =
            """
    SELECT
    b.id AS id,
    b.course_id AS courseId,
    c.title AS courseTitle,
    b.name AS name,
    b.is_seat_limited AS isSeatLimited,
    b.max_seat_limit AS maxSeatsLimit,
    b.batch_status AS batchStatus,
    b.enrollment_status AS enrollmentStatus,
    b.start_date AS startDate,
    b.start_time AS startTime,
    COUNT(sbe.student_id) AS totalEnrollments,
    b.created_date AS createdDate,
    b.last_modified_date AS lastModifiedDate
    FROM batch b
    INNER JOIN courses c ON b.course_id = c.id
    LEFT JOIN student_batch_enrollment sbe ON b.id = sbe.batch_id AND sbe.status = 'ACTIVE'
    WHERE
    ( :id IS NULL OR b.id = :id )
    AND ( :batchName IS NULL OR b.name LIKE CONCAT('%', :batchName, '%') )
    AND ( :batchStatus IS NULL OR b.batch_status = :batchStatus )
    AND ( :enrollmentStatus IS NULL OR b.enrollment_status = :enrollmentStatus )
    AND ( :courseId IS NULL OR b.course_id = :courseId )
    AND ( :instituteId IS NULL OR c.institute_id = :instituteId )
    GROUP BY
    b.id,
    b.course_id,
    c.title,
    b.name,
    b.is_seat_limited,
    b.batch_status,
    b.enrollment_status,
    b.start_date,
    b.start_time,
    b.created_date,
    b.last_modified_date
    """,
    countQuery = """
    SELECT COUNT(*)
    FROM batch b
    INNER JOIN courses c ON b.course_id = c.id
    WHERE
    ( :id IS NULL OR b.id = :id )
    AND ( :batchName IS NULL OR b.name LIKE CONCAT('%', :batchName, '%') )
    AND ( :batchStatus IS NULL OR b.batch_status = :batchStatus )
    AND ( :enrollmentStatus IS NULL OR b.enrollment_status = :enrollmentStatus )
    AND ( :courseId IS NULL OR b.course_id = :courseId )
    AND ( :instituteId IS NULL OR c.institute_id = :instituteId )
    """
    ,nativeQuery = true)
   Page<BatchDetailedProjection> getBatches(Long id, String batchName, String batchStatus, String enrollmentStatus,Long courseId, Long instituteId, Pageable pageable);
}
