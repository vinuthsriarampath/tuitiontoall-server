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

package edu.vinu.domain.module.mapper;

import edu.vinu.domain.module.entity.ModuleEntity;
import edu.vinu.domain.module.repository.projection.StudentModuleProjection;
import edu.vinu.domain.module.response.ModuleResponse;
import edu.vinu.domain.module.response.StudentModuleResponse;

public class ModuleMapper {
    public static StudentModuleResponse toStudentModuleResponse(StudentModuleProjection p) {
        return StudentModuleResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .status(p.getStatus())
                .batchId(p.getBatchId())
                .build();
    }

    public static ModuleResponse toModuleResponse(ModuleEntity entity){
        return ModuleResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus())
                .batchId(entity.getBatch().getId())
                .teacherId(entity.getTeacher().getId())
                .createdDate(entity.getCreatedAt())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }
}
