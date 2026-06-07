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

package edu.vinu.domain.chapter.mapper;

import edu.vinu.domain.chapter.entity.ChapterEntity;
import edu.vinu.domain.chapter.repository.projection.ChapterDetailedProjection;
import edu.vinu.domain.chapter.response.ChapterDetailedResponse;
import edu.vinu.domain.chapter.response.ChapterResponse;
import edu.vinu.domain.module.response.ModuleResponse;
import org.springframework.stereotype.Component;

@Component
public class ChapterMapper {
    public static ChapterResponse toChapterResponse(ChapterEntity chapterEntity){
        return ChapterResponse.builder()
                .id(chapterEntity.getId())
                .title(chapterEntity.getTitle())
                .chapterOrder(chapterEntity.getChapterOrder())
                .moduleId(chapterEntity.getModule().getId())
                .status(chapterEntity.getStatus())
                .createdDate(chapterEntity.getCreatedDate())
                .lastModifiedDate(chapterEntity.getLastModifiedDate())
                .build();
    }

    public static ChapterDetailedResponse toChapterDetailedResponse(ChapterDetailedProjection proj){
        return ChapterDetailedResponse.builder()
                .id(proj.getId())
                .title(proj.getTitle())
                .status(proj.getStatus())
                .chapterOrder(proj.getChapterOrder())
                .createdDate(proj.getCreatedDate())
                .lastModifiedDate(proj.getLastModifiedDate())
                .module(ModuleResponse.builder()
                        .id(proj.getModuleId())
                        .name(proj.getModuleName())
                        .status(proj.getModuleStatus())
                        .batchId(proj.getModuleBatchId())
                        .teacherId(proj.getModuleTeacherId())
                        .createdDate(proj.getCreatedDate())
                        .lastModifiedDate(proj.getLastModifiedDate())
                        .build())
                .build();
    }
}
