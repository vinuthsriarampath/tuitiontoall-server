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

package edu.vinu.service.common.impl;

import edu.vinu.entity.ChapterEntity;
import edu.vinu.entity.ModuleEntity;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.repository.ChapterRepository;
import edu.vinu.request.chapter.ChapterCreateRequest;
import edu.vinu.response.FieldError;
import edu.vinu.response.chapter.ChapterResponse;
import edu.vinu.service.common.ChapterService;
import edu.vinu.service.common.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final ModuleService moduleService;

    @Override
    public ChapterResponse createChapter(ChapterCreateRequest request) {

        List<FieldError> errors = new ArrayList<>();

        ModuleEntity moduleEntity = this.moduleService.getModuleEntityById(request.moduleId());

        if(moduleEntity == null){
            errors.add(new FieldError("moduleId", "Module does not exist"));
        }

        if(isChapterExistsInModule(request.moduleId(), request.title())){
            errors.add(new FieldError("name", "Chapter with the same name already exists in the module"));
        }

        if(!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }

        ChapterEntity chapterEntity = ChapterEntity.builder()
                .title(request.title())
                .status(request.status())
                .chapterOrder(findNextChapterOrderNumber(request.moduleId()))
                .module(moduleEntity)
                .build();

        return mapEntityToChapterResponse(chapterRepository.save(chapterEntity));
    }

    private boolean isChapterExistsInModule(Long moduleId,String chapterName){
        return chapterRepository.existsByModuleIdAndTitle(moduleId, chapterName);
    }

    private boolean isChapterOrderExistsInModule(Long moduleId,Long chapterOrder){
        return chapterRepository.existsByModuleIdAndChapterOrder(moduleId, chapterOrder);

    }

    private int findNextChapterOrderNumber(Long moduleId){
        return countByModuleId(moduleId) + 1;
    }

    private int countByModuleId(Long moduleId){
        return chapterRepository.countByModuleId(moduleId);
    }

    private ChapterResponse mapEntityToChapterResponse(ChapterEntity entity){
        return ChapterResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .chapterOrder(entity.getChapterOrder())
                .moduleId(entity.getModule().getId())
                .status(entity.getStatus())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }
}
