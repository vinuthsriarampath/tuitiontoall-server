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

package edu.vinu.mapper;

import edu.vinu.entity.ChapterEntity;
import edu.vinu.response.chapter.ChapterResponse;
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
}
