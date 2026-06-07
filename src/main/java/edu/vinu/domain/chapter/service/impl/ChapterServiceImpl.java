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

package edu.vinu.domain.chapter.service.impl;

import edu.vinu.domain.assignment.service.ChapterAssignmentQueryService;
import edu.vinu.domain.chapter.service.ChapterService;
import edu.vinu.domain.lecture_record.service.LectureRecordQueryService;
import edu.vinu.domain.module.service.ModuleService;
import edu.vinu.domain.chapter.entity.ChapterEntity;
import edu.vinu.domain.module.entity.ModuleEntity;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.chapter.mapper.ChapterMapper;
import edu.vinu.domain.chapter.repository.ChapterRepository;
import edu.vinu.domain.resource.service.ResourceQueryService;
import edu.vinu.domain.schedule_lecture.service.ScheduleLectureQueryService;
import edu.vinu.domain.assignment.request.chapter_assignments.ChapterAssignmentFilterRequest;
import edu.vinu.domain.chapter.request.ChapterCreateRequest;
import edu.vinu.domain.chapter.request.ChapterDetailsUpdateRequest;
import edu.vinu.domain.chapter.request.ChapterOrderRequest;
import edu.vinu.domain.chapter.request.ChapterReorderRequest;
import edu.vinu.domain.resource.request.ResourceFilterRequest;
import edu.vinu.domain.schedule_lecture.request.ScheduleLectureFilterRequest;
import edu.vinu.common.dto.FieldError;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.assignment.response.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.domain.chapter.response.ChapterDetailedResponse;
import edu.vinu.domain.chapter.response.ChapterResponse;
import edu.vinu.domain.lecture_record.response.LectureRecordResponse;
import edu.vinu.domain.resource.response.ResourceResponse;
import edu.vinu.domain.schedule_lecture.response.ScheduleLectureResponse;
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
