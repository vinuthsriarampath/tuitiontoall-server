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

package edu.vinu.domain.institute.entity;

import edu.vinu.domain.user.entity.TeacherEntity;
import edu.vinu.enums.InstituteTeacherStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "institute_teacher")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstituteTeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "instituteId",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private InstituteEntity institute;

    @JoinColumn(name = "teacherId",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TeacherEntity teacher;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status",nullable = false)
    private InstituteTeacherStatus status;

    @CreationTimestamp
    @JoinColumn(name = "joined_date",nullable = false)
    private LocalDateTime joinedDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
