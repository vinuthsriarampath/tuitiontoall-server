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

package edu.vinu.domain.course.entity;


import edu.vinu.domain.course.enums.*;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.entity.AnnouncementEntity;
import edu.vinu.domain.batch.entity.BatchEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class CourseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "duration_in_hours", nullable = false)
    private Integer durationInHours;

    @Column(name = "price", nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private CourseCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private CourseLevel level;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CourseStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private CourseLanguage language;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false)
    private CourseMode mode;

    @Nullable
    private String thumbnail;

    @Column(name = "avg_rating", nullable = false, precision = 2, scale = 1)
    private BigDecimal avg_rating= BigDecimal.ZERO;

    @Column(name = "total_no_ratings", nullable = false)
    private Integer total_no_ratings=0;

    @ManyToOne
    @JoinColumn(name="institute_id", nullable = false)
    private InstituteEntity institute;

    @OneToMany(mappedBy = "course")
    private List<BatchEntity> batchEntities = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<AnnouncementEntity> announcements = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_date",nullable = false,updatable = false)
    private LocalDateTime creationTimeStamp;

    @UpdateTimestamp
    @Column(name = "last_modified_date",insertable = false)
    private LocalDateTime updatedAt;
}
