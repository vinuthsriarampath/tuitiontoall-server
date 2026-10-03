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

package edu.vinu.domain.application.mappers;

import edu.vinu.domain.application.repository.projection.TeacherApplicationProjection;
import edu.vinu.domain.application.response.TeacherApplicationResponse;

public class ApplicationMapper {
    public static TeacherApplicationResponse toTeacherApplicationResponse(TeacherApplicationProjection p){
        return TeacherApplicationResponse.builder()
                .applicationId(p.getApplicationId())
                .vacancyId(p.getVacancyId())
                .vacancyTitle(p.getVacancyTitle())
                .instituteId(p.getInstituteId())
                .instituteName(p.getInstituteName())
                .instituteUserSlug(p.getInstituteUserSlug())
                .applicationStatus(p.getApplicationStatus())
                .appliedDate(p.getAppliedDate())
                .lastModifiedDate(p.getLastModifiedDate())
                .build();
    }
}
