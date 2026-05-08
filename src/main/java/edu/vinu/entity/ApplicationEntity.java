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
import edu.vinu.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_teacher_vacancy_application", columnNames = {"teacher_id", "teacher_vacancy_id"})
        })
@Builder
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherEntity teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_vacancy_id", nullable = false)
    private TeacherVacancyEntity teacherVacancy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private ApplicationStatus status;

    @CreationTimestamp
    @Column(name = "applied_date", updatable = false, nullable = false)
    private LocalDateTime appliedDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date", insertable = false)
    private LocalDateTime lastModifiedDate;
}
