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

package edu.vinu.entity;

import edu.vinu.enums.ScheduleLectureStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "schedule_lecture",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_chapter_start_date_time_end_time", columnNames = {"chapter_id", "start_date", "start_time", "end_time"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleLectureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chapter_id",nullable = false)
    private ChapterEntity chapter;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "start_date",nullable = false)
    private LocalDate startDate;

    @Column(name = "start_time",nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time",nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScheduleLectureStatus status;

    @Column(name = "late_attendance",nullable = false)
    private boolean lateAttendance = true;

    @Column(name = "meeting_url",nullable = false)
    private String meetingUrl;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date",insertable = false)
    private LocalDateTime lastModifiedDate;
}
