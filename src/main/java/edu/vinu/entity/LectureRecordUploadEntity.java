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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lecture_record_upload")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureRecordUploadEntity {
    @Id
    private String uploadId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate recordedDate;

    @Column(nullable = false)
    private Long chapterId;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private Long totalSize;

    @Column(nullable = false)
    private Integer totalChunks;

    @Column(nullable = false)
    private Integer uploadedChunks;

    @Column(nullable = false)
    private Boolean completed;

    @CreationTimestamp
    private LocalDateTime createdDate;
}
