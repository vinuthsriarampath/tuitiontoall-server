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
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.mapper.ChapterMapper;
import edu.vinu.repository.ChapterRepository;
import edu.vinu.request.chapter.ChapterCreateRequest;
import edu.vinu.request.chapter.ChapterDetailsUpdateRequest;
import edu.vinu.request.chapter.ChapterOrderRequest;
import edu.vinu.request.chapter.ChapterReorderRequest;
import edu.vinu.response.FieldError;
import edu.vinu.response.chapter.ChapterResponse;
import edu.vinu.service.common.ChapterService;
import edu.vinu.service.common.ModuleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public ChapterResponse updateChapterDetailsById(Long id, ChapterDetailsUpdateRequest request) {
        ChapterEntity chapterEntity = this.getChapterEntityById(id);
        List<FieldError> errors = new ArrayList<>();

        if(!request.moduleId().equals(chapterEntity.getModule().getId())){
            chapterEntity.setModule(moduleService.getModuleEntityById(request.moduleId()));
            chapterEntity.setChapterOrder(findNextChapterOrderNumber(request.moduleId()));
        }

        if(!request.title().equals(chapterEntity.getTitle()) || !request.moduleId().equals(chapterEntity.getModule().getId()) ){
            if(isChapterExistsInModule(request.moduleId(), request.title())){
                errors.add(new FieldError("name", "Chapter with the same name already exists in the module"));
            }else{
                chapterEntity.setTitle(request.title());
            }
        }

        chapterEntity.setStatus(request.status());

        if(!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }
        return mapEntityToChapterResponse(chapterRepository.save(chapterEntity));
    }

    @Transactional
    @Override
    public List<ChapterResponse> reorderChapters(ChapterReorderRequest request) {
        List<Long> ids = request.chapters()
                .stream()
                .map(ChapterOrderRequest::chapterId)
                .toList();

        List<ChapterEntity> chapters = chapterRepository.findAllById(ids);

        // STEP 1: break constraint temporarily
        for (ChapterEntity chapter : chapters) {
            chapter.setChapterOrder(-chapter.getChapterOrder());
        }
        chapterRepository.saveAll(chapters);
        chapterRepository.flush();

        // STEP 2: apply final ordering
        Map<Long, Integer> orderMap = request.chapters()
                .stream()
                .collect(Collectors.toMap(
                        ChapterOrderRequest::chapterId,
                        ChapterOrderRequest::chapterOrder
                ));

        for (ChapterEntity chapter : chapters) {
            chapter.setChapterOrder(orderMap.get(chapter.getId()));
        }

        return chapterRepository.saveAll(chapters)
                .stream()
                .sorted(Comparator.comparingInt(ChapterEntity::getChapterOrder))
                .map(ChapterMapper::toChapterResponse)
                .toList();
    }

    private ChapterEntity getChapterEntityById(Long id){
        return chapterRepository.findById(id).orElseThrow(() -> new NotFoundException("Chapter with the given id does not exist"));
    }

    private boolean isChapterExistsInModule(Long moduleId,String chapterName){
        return chapterRepository.existsByModuleIdAndTitle(moduleId, chapterName);
    }

    private boolean isChapterOrderExistsInModule(Long moduleId,int chapterOrder){
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
