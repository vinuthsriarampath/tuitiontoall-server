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

package edu.vinu.domain.teacher_vacancy.mapper;

import edu.vinu.domain.teacher_vacancy.dto.TeacherVacancy;
import edu.vinu.domain.teacher_vacancy.entity.TeacherVacancyEntity;

public class TeacherVacancyMapper {
    public static TeacherVacancy toTeacherVacancy(TeacherVacancyEntity e) {
        return TeacherVacancy.builder()
                .id(e.getId())
                .title(e.getTitle())
                .requiredExperienceYears(e.getRequiredExperienceYears())
                .jobDescription(e.getJobDescription())
                .status(e.getStatus())
                .vacancyClosingDate(e.getVacancyClosingDate())
                .instituteId(e.getInstitute().getId())
                .build();
    }
}
