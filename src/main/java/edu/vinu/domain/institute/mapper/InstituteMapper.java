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

package edu.vinu.domain.institute.mapper;

import edu.vinu.domain.institute.dto.Institute;
import edu.vinu.domain.institute.entity.InstituteEntity;

public class InstituteMapper {
    public static Institute toInstitute(InstituteEntity instituteEntity){
        return Institute.builder()
                .id(instituteEntity.getId())
                .instituteName(instituteEntity.getInstituteName())
                .build();
    }
}
