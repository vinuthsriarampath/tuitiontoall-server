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

import edu.vinu.domain.module.service.ModuleService;
import edu.vinu.entity.ChapterEntity;
import edu.vinu.domain.module.entity.ModuleEntity;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.mapper.ChapterMapper;
import edu.vinu.repository.ChapterRepository;
import edu.vinu.request.assignments.chapter_assignments.ChapterAssignmentFilterRequest;
import edu.vinu.request.chapter.ChapterCreateRequest;
import edu.vinu.request.chapter.ChapterDetailsUpdateRequest;
import edu.vinu.request.chapter.ChapterOrderRequest;
import edu.vinu.request.chapter.ChapterReorderRequest;
import edu.vinu.request.resource.ResourceFilterRequest;
import edu.vinu.request.schedule_lecture.ScheduleLectureFilterRequest;
import edu.vinu.common.dto.FieldError;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.response.assignments.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.response.chapter.ChapterDetailedResponse;
import edu.vinu.response.chapter.ChapterResponse;
import edu.vinu.response.lecture_record.LectureRecordResponse;
import edu.vinu.response.resource.ResourceResponse;
import edu.vinu.response.schedule_lecture.ScheduleLectureResponse;
import edu.vinu.service.common.*;
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
    private final LectureRecordQueryService lectureRecordQueryService;
    private final ScheduleLectureQueryService scheduleLectureQueryService;
    private final ResourceQueryService resourceQueryService;
    private final ChapterAssignmentQueryService chapterAssignmentQueryService;
    @Override
    public ChapterResponse createChapter(ChapterCreateRequest request) {

        List<FieldError> errors = new ArrayList<>();

        ModuleEntity moduleEntity = this.moduleService.getModuleEntityById(request.moduleId());

        if(moduleEntity == null){
            errors.add(new FieldError("moduleId", "Module does not exist"));
        }

        if(isChapterExistsInModule(request.moduleId(), request.title())){
            errors.add(new FieldError("title", "Chapter with the same title already exists in the module"));
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
                errors.add(new FieldError("title", "Chapter with the same title already exists in the module"));
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

    @Override
    public ChapterDetailedResponse getDetailedChapterById(Long id) {
        return chapterRepository.findDetailedById(id).map(ChapterMapper::toChapterDetailedResponse).orElseThrow(() -> new NotFoundException("Chapter with the given id does not exist"));
    }

    @Override
    public ChapterEntity getChapterEntityById(Long id){
        return chapterRepository.findById(id).orElseThrow(() -> new NotFoundException("Chapter with the given id does not exist"));
    }

    @Override
    public List<LectureRecordResponse> getAllLectureRecordsByChapterId(Long id) {
        return lectureRecordQueryService.getAllLectureRecordsByChapterId(id);
    }

    @Override
    public PaginatedApiResponse<ScheduleLectureResponse> getAllScheduleLecturesByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ScheduleLectureFilterRequest filters) {
        return scheduleLectureQueryService.getAllScheduleLecturesByChapter(chapterId, page, size, direction, sortBy, filters);
    }

    @Override
    public PaginatedApiResponse<ResourceResponse> getAllResourcesByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ResourceFilterRequest filters) {
        return resourceQueryService.getAllResourcesByChapter(chapterId, page, size, direction, sortBy, filters);
    }

    @Override
    public PaginatedApiResponse<ChapterAssignmentResponse> getAllChapterAssignmentsByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ChapterAssignmentFilterRequest filters) {
        return chapterAssignmentQueryService.getAllChapterAssignmentByChapter(chapterId, page, size, direction, sortBy, filters);
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
