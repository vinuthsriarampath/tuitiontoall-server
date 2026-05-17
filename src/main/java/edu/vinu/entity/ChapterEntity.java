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

import edu.vinu.enums.ChapterStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chapter",
    uniqueConstraints = {
        @UniqueConstraint(name = "un_moduleid_chapterorder", columnNames = {"module_id", "chapter_order"}),
        @UniqueConstraint(name = "un_moduleid_title", columnNames = {"module_id", "title"})
    }
)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChapterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private ModuleEntity module;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "chapter_order", nullable = false)
    private int chapterOrder;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChapterStatus status;
    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;
    @Column(name = "last_modified_date", insertable = false)
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
