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

import edu.vinu.entity.LectureRecordEntity;
import edu.vinu.response.lecture_record.LectureRecordResponse;
import org.springframework.stereotype.Component;

@Component
public class LectureRecordMapper {

    public static LectureRecordResponse toLectureRecordResponse(LectureRecordEntity entity){
        return LectureRecordResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .url(entity.getUrl())
                .chapterId(entity.getChapter().getId())
                .recordedDate(entity.getRecordedDate())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }

}
