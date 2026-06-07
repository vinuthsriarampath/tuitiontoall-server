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

import edu.vinu.domain.batch.entity.BatchEntity;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.enums.AnnouncementStatus;
import edu.vinu.enums.AnnouncementVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description",nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility",nullable = false)
    private AnnouncementVisibility visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private AnnouncementStatus status;

    @Column(name = "is_pinned",nullable = false)
    private boolean isPinned;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(name = "expire_at",nullable = false)
    private LocalDateTime expireAt;

    @CreationTimestamp
    @Column(name = "created_date",nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @JoinColumn(name = "institute_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private InstituteEntity institute;

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @JoinColumn(name = "batch_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BatchEntity batch;
}
