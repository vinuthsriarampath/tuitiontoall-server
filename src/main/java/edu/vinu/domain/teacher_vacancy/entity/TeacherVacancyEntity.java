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

package edu.vinu.domain.teacher_vacancy.entity;

import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.application.entity.ApplicationEntity;
import edu.vinu.domain.teacher_vacancy.enums.TeacherVacancyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "teacher_vacancy")
public class TeacherVacancyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private java.lang.String title;

    @Column(name = "required_experience_years",nullable = false)
    private Integer requiredExperienceYears;

    @Column(name = "job_description", columnDefinition = "TEXT",nullable = false)
    private java.lang.String jobDescription;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private TeacherVacancyStatus status;

    @Column(name = "vacancy_closing_date",nullable = false)
    private LocalDateTime vacancyClosingDate;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false,nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date",insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instituteId",nullable = false)
    private InstituteEntity institute;

    @OneToMany(mappedBy = "teacherVacancy")
    private List<ApplicationEntity> applications = new ArrayList<>();
}
