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

import edu.vinu.entity.user_entities.TeacherEntity;
import edu.vinu.enums.ModuleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "module",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_batch_module_name",columnNames = {"name","batch_id"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private ModuleStatus status = ModuleStatus.DRAFT;
    @CreationTimestamp
    @Column(name = "created_date",nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "last_modified_date",insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "batch_id",nullable = false)
    private BatchEntity batch;

    @ManyToOne
    @JoinColumn(name = "teacher_id",nullable = false)
    private TeacherEntity teacher;
}
