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

package edu.vinu.domain.lecture_record.entity;

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
@Table(name = "lecture_record_upload",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_chapter_title_upload", columnNames = {"title", "chapter_id"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureRecordUploadEntity {
    @Id
    @Column(name = "upload_id")
    private String uploadId;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "recorded_date")
    private LocalDate recordedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    private ChapterEntity chapter;

    @Column(nullable = false,name = "original_file_name")
    private String originalFileName;

    @Column(nullable = false, name = "total_size")
    private Long totalSize;

    @Column(nullable = false, name = "total_chunks")
    private Integer totalChunks;

    @Column(nullable = false,name = "uploaded_chunks")
    private Integer uploadedChunks;

    @Column(nullable = false, name = "completed")
    private Boolean completed;

    @OneToOne
    @JoinColumn(name = "lecture_record_id")
    private LectureRecordEntity lectureRecord;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date",insertable = false)
    private LocalDateTime lastModifiedDate;
}
