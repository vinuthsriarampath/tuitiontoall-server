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

import edu.vinu.domain.chapter.entity.ChapterEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lecture_recording",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_chapter_title",columnNames = {"title","chapter_id"})
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title",nullable = false)
    private String title;
    @Column(name = "url",nullable = false)
    private String url;
    @Column(name = "recorded_date",nullable = false)
    private LocalDate recordedDate;
    @Column(name = "created_date",updatable = false,nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;
    @Column(name = "last_modified_date", insertable = false)
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "chapter_id",nullable = false)
    private ChapterEntity chapter;

}
