/*
 * Copyright (c) 2025 vinuth sri arampath
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


import edu.vinu.common.BaseAuditingEntity;
import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class CourseEntity extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private int durationInHours;
    private Double price;
    private CourseCategory category;
    private CourseLevel level;
    private CourseStatus status;
    private CourseLanguage language;
    private CourseMode mode;
    @ManyToOne
    @JoinColumn(name="institute_id", nullable = false)
    private InstituteEntity institute;
}
