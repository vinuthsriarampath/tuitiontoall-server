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

package edu.vinu.repository;

import edu.vinu.entity.ScheduleLectureEntity;
import edu.vinu.enums.ScheduleLectureStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ScheduleLectureRepository extends JpaRepository<ScheduleLectureEntity,Long> {
    @Query(value = """
    SELECT EXISTS (
        SELECT 1
        FROM schedule_lecture sl
        WHERE sl.chapter_id = :chapterId
        AND sl.start_date = :startDate
        AND (:excludedId IS NULL OR sl.id != :excludedId)
        AND (
            (:startTime < sl.end_time AND :endTime > sl.start_time)
        )
    )
""",nativeQuery = true)
    Integer existsInSameTimePeriodInChapter(Long chapterId, LocalDate startDate, LocalTime startTime, LocalTime endTime, Long excludedId);

    @Query(value = """
    SELECT
        sl.id,
        sl.chapter_id,
        sl.topic,
        sl.start_date,
        sl.start_time,
        sl.end_time,
        sl.late_attendance,
        sl.meeting_url,
        sl.status,
        sl.created_date,
        sl.last_modified_date
    FROM schedule_lecture sl
    WHERE (:chapterId IS NULL OR sl.chapter_id = :chapterId)
        AND (:id IS NULL OR sl.id = :id)
        AND (:start_date IS NULL OR sl.start_date = :start_date)
        AND (:start_time IS NULL OR sl.start_time >= :start_time)
        AND (:end_time IS NULL OR sl.end_time <= :end_time)
        AND (:status IS NULL OR sl.status = :status)
    """,
    countQuery = """
    SELECT COUNT(*)
    FROM schedule_lecture sl
    WHERE (:chapterId IS NULL OR chapter_id = :chapterId)
        AND (:id IS NULL OR id = :id)
        AND (:start_date IS NULL OR start_date = :start_date)
        AND (:start_time IS NULL OR start_time >= :start_time)
        AND (:end_time IS NULL OR end_time <= :end_time)
        AND (:status IS NULL OR status = :status)
    """,
    nativeQuery = true)
    Page<ScheduleLectureEntity> getAllModules(Long chapterId, Long id, LocalDate start_date, LocalTime start_time, LocalTime end_time, String status, Pageable pageable);
}
