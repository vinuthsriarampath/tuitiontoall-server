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

package edu.vinu.domain.resource.entity;

import edu.vinu.domain.chapter.entity.ChapterEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resource_upload",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_chapter_id_name",columnNames = {"chapter_id","name"})
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceUploadEntity {
    @Id
    private String uploadId;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = false)
    private ChapterEntity chapter;

    @OneToOne
    @JoinColumn(name = "resource_id")
    private ResourceEntity resource;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @Column(name = "completed")
    private boolean completed = false;

    @Column(name = "total_size",nullable = false)
    private Long totalSize;

    @Column(name = "total_chunks",nullable = false)
    private Integer totalChunks;

    @Column(name = "uploaded_chunks",nullable = false)
    private Integer uploadedChunks;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date",insertable = false)
    private LocalDateTime lastModifiedDate;
}
