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

package edu.vinu.entity.user_entities;

import edu.vinu.entity.CourseEntity;
import edu.vinu.entity.TeacherVacancyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "institute")
public class InstituteEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instituteName;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "institute")
    private List<CourseEntity> courses = new ArrayList<>();

    @OneToMany(mappedBy = "institute")
    private List<TeacherVacancyEntity> teacherVacancies = new ArrayList<>();
}
