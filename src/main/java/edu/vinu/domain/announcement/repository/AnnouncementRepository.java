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

package edu.vinu.domain.announcement.repository;

import edu.vinu.domain.announcement.entity.AnnouncementEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {

    @Query(value = """
    SELECT a.*
    FROM announcements a
    JOIN courses c ON a.course_id = c.id
    WHERE a.institute_id = :instituteId
    AND (:visibility IS NULL OR a.visibility = :visibility)
    AND (:status IS NULL OR a.status = :status)
    AND (:courseId IS NULL OR (a.course_id = :courseId AND (c.institute_id = :instituteId)))
    AND (:batchId IS NULL OR a.batch_id = :batchId)
    """,
    countQuery = """
    SELECT COUNT(a.id)
    FROM announcements a
    JOIN courses c ON a.course_id = c.id
    WHERE a.institute_id = :instituteId
    AND (:visibility IS NULL OR a.visibility = :visibility)
    AND (:status IS NULL OR a.status = :status)
    AND (:courseId IS NULL OR (a.course_id = :courseId AND (c.institute_id = :instituteId)))
    AND (:batchId IS NULL OR a.batch_id = :batchId)
    """, nativeQuery = true)
    Page<AnnouncementEntity> findAllForInstitute(@Param("instituteId") Long instituteId, @Param("visibility") String visibility, @Param("status") String status, @Param("courseId") Long courseId, @Param("batchId") Long batchId, Pageable pageable);

    @Query(value = """
    SELECT DISTINCT a.*
    FROM announcements a
    INNER JOIN batch b ON (
            (a.visibility = 'BATCH' AND a.batch_id = b.id) OR (a.visibility = 'COURSE' AND a.course_id = b.course_id)
    )
    INNER JOIN student_batch_enrollment sbe ON sbe.student_id = :studentId
    WHERE sbe.student_id = :studentId
    AND a.visibility IN ('COURSE','BATCH')
    AND (:status IS NULL OR a.status = :status)
    AND (:courseId IS NULL OR a.course_id = :courseId)
    AND (:batchId IS NULL OR a.batch_id = :batchId)
    """,
    countQuery = """
    SELECT count(DISTINCT a.id)
    FROM announcements a
    INNER JOIN batch b ON (
            (a.visibility = 'BATCH' AND a.batch_id = b.id) OR (a.visibility = 'COURSE' AND a.course_id = b.course_id)
    )
    INNER JOIN student_batch_enrollment sbe ON sbe.student_id = :studentId
    WHERE sbe.student_id = :studentId
    AND a.visibility IN ('COURSE','BATCH')
    AND (:status IS NULL OR a.status = :status)
    AND (:courseId IS NULL OR a.course_id = :courseId)
    AND (:batchId IS NULL OR a.batch_id = :batchId)
    """, nativeQuery = true)
    Page<AnnouncementEntity> findAllForStudent(@Param("studentId") Long studentId, @Param("status") String status, @Param("courseId") Long courseId, @Param("batchId") Long batchId, Pageable pageable);

    @Query(value = """
    SELECT a.*
    FROM announcements a
    WHERE
    (
            ( a.visibility = 'ALL_TEACHERS' AND (:instituteId IS NULL OR a.institute_id = :instituteId) )
        OR (a.visibility IN ('COURSE','BATCH')
                AND EXISTS(
                        SELECT 1
                        FROM module m
                        JOIN batch b ON m.batch_id = b.id
                        WHERE m.teacher_id = :teacherId
                        AND b.course_id = a.course_id
                        AND ( a.visibility = 'COURSE'
                            OR (
                                a.visibility = 'BATCH'
                                AND a.batch_id = b.id
                            )
                        )
                    )
                )
    )
    AND (:status IS NULL OR a.status = :status)
    AND (:courseId IS NULL OR a.course_id = :courseId)
    AND (:batchId IS NULL OR a.batch_id = :batchId)
    """,countQuery = """
    SELECT COUNT(a.id)
    FROM announcements a
    WHERE
    (
            ( a.visibility = 'ALL_TEACHERS' AND (:instituteId IS NULL OR a.institute_id = :instituteId) )
        OR (a.visibility IN ('COURSE','BATCH')
                AND EXISTS(
                        SELECT 1
                        FROM module m
                        JOIN batch b ON m.batch_id = b.id
                        WHERE m.teacher_id = :teacherId
                        AND b.course_id = a.course_id
                        AND ( a.visibility = 'COURSE'
                            OR (
                                a.visibility = 'BATCH'
                                AND a.batch_id = b.id
                            )
                        )
                    )
                )
    )
    AND (:status IS NULL OR a.status = :status)
    AND (:courseId IS NULL OR a.course_id = :courseId)
    AND (:batchId IS NULL OR a.batch_id = :batchId)
    """, nativeQuery = true)
    Page<AnnouncementEntity> findAllForTeacher(@Param("teacherId") Long teacherId,@Param("instituteId") Long instituteId, @Param("status") String status, @Param("courseId") Long courseId, @Param("batchId") Long batchId, Pageable pageable);

    @Query(value =  """
    SELECT COUNT(*)
    FROM announcements a
    WHERE a.institute_id = :instituteId
    AND (:status IS NULL OR a.status = :status)
    AND (:pinned IS NULL OR a.is_pinned = :pinned)
    AND (:visibility IS NULL OR a.visibility = :visibility)
    AND ( :visibility IS NULL OR (:visibility = 'COURSE' AND a.course_id = :courseId)
        OR (:visibility = 'BATCH' AND a.course_id = :courseId AND a.batch_id = :batchId)
        OR (:visibility IN ('PRIVATE', 'ALL_TEACHERS'))
    );
    """, nativeQuery = true)
    int countAnnouncementsByInstitute(@Param("instituteId") Long instituteId, @Param("status") String status, @Param("visibility") String visibility, @Param("pinned") Boolean pinned, @Param("courseId") Long courseId, @Param("batchId") Long batchId);


    @Modifying
    @Transactional
    @Query(value = """
    UPDATE announcements
    SET status = :expireStatus, is_pinned = false
    WHERE expire_at <= CURRENT_TIMESTAMP
    AND status = :publishedStatus
    """,nativeQuery = true)
    int expireAnnouncementsByExpireAt(@Param("expireStatus") String expireStatus, @Param("publishedStatus") String publishedStatus);

    @Query(value = """
    SELECT EXISTS (
        SELECT 1
        FROM announcements
        WHERE id = :announcementId
          AND institute_id = :instituteId
    ) AS is_owner;
    """,nativeQuery = true)
    int isOwnerOfAnnouncement(Long announcementId, Long instituteId);


    @Query(value = """
    SELECT EXISTS(
        SELECT 1
        FROM announcements a
        JOIN batch b ON (
            (a.visibility = 'BATCH' AND a.batch_id = b.id) OR (a.visibility = 'COURSE' AND a.course_id = b.course_id)
        )
        JOIN student_batch_enrollment sbe ON sbe.batch_id = b.id
        WHERE a.id = :announcementId
        AND sbe.student_id = :studentId
        AND a.visibility IN ('COURSE','BATCH')
    )
    """, nativeQuery = true)
    int canStudentViewAnnouncement(Long announcementId, Long studentId);

    @Query(value = """
    SELECT EXISTS(
            SELECT 1
                FROM announcements a
            WHERE a.id = :announcementId
                AND (
                        a.visibility = 'ALL_TEACHERS'
                        OR EXISTS(
                                    SELECT 1
                                        FROM module m
                                        JOIN batch b ON b.id = m.batch_id
                                    WHERE m.teacher_id = :teacherId
                                        AND (
                                                (a.visibility = 'COURSE' and a.course_id = b.course_id)
                                                OR
                                                (a.visibility = 'BATCH' and a.batch_id = b.id)
                                            )
                            )
                    )
        )
    """,nativeQuery = true)
    int canTeacherViewAnnouncement(Long announcementId, Long teacherId);
}
