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

package edu.vinu.domain.assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "file_name",nullable = false)
    private String fileName;

    @Column(name = "total_marks",nullable = false)
    private Integer totalMarks;

    @Column(name = "available_on",nullable = false)
    private LocalDateTime availableOn;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "late_submission", nullable = false)
    private boolean lateSubmission;

    @Column(name = "resubmission",nullable = false)
    private boolean resubmission;

    @Column(name = "max_attempts",nullable = false)
    private Integer maxAttempts;

    @CreationTimestamp
    @Column(name = "created_date",nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date",insertable = false)
    private LocalDateTime lastModifiedDate;
}
