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

package edu.vinu.domain.course.repository;

import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.reporting.projection.TrendPointProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    List<CourseEntity> findAllByInstituteId(Long id);

    @Query(value = """
                    SELECT *
                    FROM courses c
                    WHERE c.institute_id = :instituteId
                    AND (:category IS NULL OR c.category = :category)
                    AND (:level IS NULL OR c.level = :level)
                    AND (:language IS NULL OR c.language = :language)
                    AND (:mode IS NULL OR c.mode = :mode)
                    AND (:status IS NULL OR c.status = :status);
            """, nativeQuery = true)
    List<CourseEntity> findAllByInstituteIdWithFilters(@Param("instituteId") Long instituteId, @Param("category") String category, @Param("level") String level, @Param("language") String language, @Param("mode") String mode, @Param("status") String status);

    @Query(value = """
    SELECT COUNT(*)
    FROM courses c
    WHERE c.status = :status
    AND c.institute_id = :instituteId
    """, nativeQuery = true)
    Long countCoursesByStatusAndInstituteId(String status, Long instituteId);

    @Query(value = """
    SELECT COUNT(*)
    FROM courses c
    WHERE c.status = :status
    AND c.institute_id = :instituteId
    AND c.created_date BETWEEN :startDateTime AND :endDateTime
    """, nativeQuery = true)
    Long countCoursesByStatusAndInstituteIdBetween(String status, Long instituteId, LocalDateTime startDateTime, LocalDateTime endDateTime);


    @Query(value = """
    SELECT
    DATE_FORMAT(c.created_date, '%Y-%m-%d %H:00:00') AS bucket,
    COUNT(*) AS value
    FROM courses c
    WHERE c.status = :status
    AND c.institute_id = :instituteId
    AND c.created_date >= :startDateTime
    AND c.created_date < :endDateTime
    GROUP BY DATE_FORMAT(c.created_date, '%Y-%m-%d %H')
    ORDER BY bucket
    """, nativeQuery = true)
    List<TrendPointProjection> getHourlyCoursesTrendByStatusAndInstitute(String status, Long instituteId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
    DATE_FORMAT(c.created_date, '%Y-%m-%d 00:00:00') AS bucket,
    COUNT(*) AS value
    FROM courses c
    WHERE c.status = :status
    AND c.institute_id = :instituteId
    AND c.created_date >= :startDateTime
    AND c.created_date < :endDateTime
    GROUP BY DATE(c.created_date)
    ORDER BY bucket
    """, nativeQuery = true)
    List<TrendPointProjection> getDailyCoursesTrendByStatusAndInstitute(String status, Long instituteId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
    DATE_FORMAT(c.created_date, '%Y-%m-01 00:00:00') AS bucket,
    COUNT(*) AS value
    FROM courses c
    WHERE c.status = :status
    AND c.institute_id = :instituteId
    AND c.created_date >= :startDateTime
    AND c.created_date < :endDateTime
    GROUP BY YEAR(c.created_date), MONTH(c.created_date)
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getMonthlyCoursesTrendByStatusAndInstitute(String status, Long instituteId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
