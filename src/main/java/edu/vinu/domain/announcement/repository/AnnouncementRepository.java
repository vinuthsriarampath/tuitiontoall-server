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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {

    @Query(
            value =
                    """
                            SELECT *
                            FROM announcements a
                            WHERE a.institute_id = :instituteId
                            AND (:visibility IS NULL OR a.visibility = :visibility)
                            AND (:status IS NULL OR a.status = :status)
                            AND (:courseId IS NULL OR a.course_id = :courseId)
                            AND (:batchId IS NULL OR a.batch_id = :batchId)
                            
                            """,
            countQuery =
                    """
                            SELECT COUNT(*)
                            FROM announcements a
                            WHERE a.institute_id = :instituteId
                            AND (:visibility IS NULL OR a.visibility = :visibility)
                            AND (:status IS NULL OR a.status = :status)
                            AND (:courseId IS NULL OR a.course_id = :courseId)
                            AND (:batchId IS NULL OR a.batch_id = :batchId)
                            """,
            nativeQuery = true)
    Page<AnnouncementEntity> findAllByInstituteWithFilters(
            @Param("instituteId") Long instituteId,
            @Param("visibility") String visibility,
            @Param("status") String status,
            @Param("courseId") Long courseId,
            @Param("batchId") Long batchId,
            Pageable pageable
    );

    @Query(value =
            """
                        SELECT
                        	COUNT(*)
                        FROM
                        	announcements a
                        WHERE
                        	a.institute_id = :instituteId
                        	AND (:status IS NULL
                        		OR a.status = :status)
                        	AND (:pinned IS NULL
                        		OR a.is_pinned = :pinned)
                        	AND (:visibility IS NULL
                        		OR a.visibility = :visibility)
                        	AND (
                                :visibility IS NULL
                        		OR (:visibility = 'COURSE'
                        			AND a.course_id = :courseId)
                        		OR (:visibility = 'BATCH'
                        			AND a.course_id = :courseId
                        			AND a.batch_id = :batchId)
                        		OR (:visibility IN ('PRIVATE', 'ALL_TEACHERS'))
                          );
                    """, nativeQuery = true)
    int countAnnouncementsByInstitute(
            @Param("instituteId") Long instituteId,
            @Param("status") String status,
            @Param("visibility") String visibility,
            @Param("pinned") Boolean pinned,
            @Param("courseId") Long courseId,
            @Param("batchId") Long batchId
    );
}
