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

import edu.vinu.entity.BatchEntity;
import edu.vinu.entity.ModuleEntity;
import edu.vinu.domain.user.entity.TeacherEntity;
import edu.vinu.enums.ModuleStatus;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.repository.ModuleRepository;
import edu.vinu.request.assignments.module_assignments.ModuleAssignmentFilterRequest;
import edu.vinu.request.modules.*;
import edu.vinu.request.modules.enums.ModuleCreateStatus;
import edu.vinu.response.BatchBasicResponse;
import edu.vinu.common.dto.FieldError;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.user.response.TeacherBasicResponse;
import edu.vinu.response.assignments.module_assignment.ModuleAssignmentResponse;
import edu.vinu.response.module.ModuleDetailedResponse;
import edu.vinu.response.module.ModuleResponse;
import edu.vinu.service.common.BatchService;
import edu.vinu.service.common.InstituteTeacherService;
import edu.vinu.service.common.ModuleAssignmentQueryService;
import edu.vinu.service.common.ModuleService;
import edu.vinu.common.util.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;
    private final BatchService batchService;
    private final InstituteTeacherService instituteTeacherService;
    private final ModuleAssignmentQueryService moduleAssignmentQueryService;
    @Override
    public ModuleResponse createModule(ModuleCreateRequest request) {
        BatchEntity batchEntity = batchService.getBatchEntityById(request.getBatchId());

        if(!batchService.isBatchOwner(batchEntity)){
            throw new InvalidInputException("You are not authorized to create module for this batch");
        }

        List<FieldError> errors = new ArrayList<>();

        if(isModuleExists(request.getName(), request.getBatchId())){
            errors.add(new FieldError("name","same module name already exists in the batch"));
        }

        TeacherEntity teacherEntity = null;
        try {
            teacherEntity = instituteTeacherService.getCurrentInstituteRelatedTeacherEntityById(request.getTeacherId());
        } catch (NotFoundException e) {
            errors.add(new FieldError("teacherId",e.getMessage()));
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }

        ModuleEntity moduleEntity = ModuleEntity.builder()
                .name(request.getName())
                .batch(batchEntity)
                .teacher(teacherEntity)
                .status(mapToModuleStatus(request.getStatus()))
                .build();

        return mapToAnnouncementResponse(moduleRepository.save(moduleEntity));
    }

    @Override
    public ModuleResponse updateModuleName(Long id, ModuleNameUpdateRequest request) {
        ModuleEntity moduleEntity = getModuleEntityById(id);

        if (!isModuleOwner(moduleEntity)) {
            throw new InvalidInputException("You are not authorized to update this module");
        }

        if (isModuleExists(request.getName(), moduleEntity.getBatch().getId())) {
            throw new InvalidInputException("name", "same module name already exists in the batch");
        }

        moduleEntity.setName(request.getName());

        return mapToAnnouncementResponse(moduleRepository.save(moduleEntity));
    }

    @Override
    public ModuleResponse publishModule(Long id) {
        ModuleEntity moduleEntity = getModuleEntityById(id);

        return mapToAnnouncementResponse(updateStatus(moduleEntity, ModuleStatus.PUBLISHED));
    }

    @Override
    public ModuleResponse lockModule(Long id) {
        ModuleEntity moduleEntity = getModuleEntityById(id);

        return mapToAnnouncementResponse(updateStatus(moduleEntity, ModuleStatus.LOCKED));
    }

    @Override
    public ModuleResponse archiveModule(Long id) {
        ModuleEntity moduleEntity = getModuleEntityById(id);

        return mapToAnnouncementResponse(updateStatus(moduleEntity, ModuleStatus.ARCHIVED));
    }

    @Override
    public Page<ModuleResponse> getAllFilteredModules(int page, int size, String direction, List<String> sortBy, ModuleFilterRequest filter) {

        Pageable pageable = PageRequest.of(page, size, SortUtil.buildSort(direction, sortBy, List.of("created_date")));
        return moduleRepository.getAllModules(pageable,filter.status(), filter.batchId())
                .map(this::mapToAnnouncementResponse);
    }

    @Override
    public ModuleResponse updateModuleTeacher(Long id, ModuleTeacherUpdateRequest request) {
        ModuleEntity moduleEntity = getModuleEntityById(id);

        if (!isModuleOwner(moduleEntity)) {
            throw new InvalidInputException("You are not authorized to update this module");
        }

        try {
            TeacherEntity teacherEntity = instituteTeacherService.getCurrentInstituteRelatedTeacherEntityById(request.teacherId());
            moduleEntity.setTeacher(teacherEntity);
            return mapToAnnouncementResponse(moduleRepository.save(moduleEntity));
        } catch (Exception e) {
            throw new InvalidInputException("teacherId", e.getMessage());
        }
    }

    @Override
    public ModuleResponse updateModuleBatch(Long id, ModuleBatchUpdateRequest request) {
        ModuleEntity moduleEntity = getModuleEntityById(id);

        if (!isModuleOwner(moduleEntity)) {
            throw new InvalidInputException("You are not authorized to update this module");
        }

        BatchEntity batchEntity;
        try {
            batchEntity = batchService.getBatchEntityById(request.batchId());
        } catch (Exception e) {
            throw new NotFoundException("Batch with id " + request.batchId() + " not found");
        }
        if(!batchService.isBatchOwner(batchEntity)){
            throw new UnauthorizedException("You are not authorized to move module to this batch");
        }

        moduleEntity.setBatch(batchEntity);
        return mapToAnnouncementResponse(moduleRepository.save(moduleEntity));

    }

    @Override
    public ModuleDetailedResponse getDetailedModuleById(Long id) {
        return moduleRepository.getDetailedModuleById(id)
                .map(dmp -> ModuleDetailedResponse.builder()
                        .id(dmp.getId())
                        .name(dmp.getName())
                        .status(dmp.getStatus())
                        .createdDate(dmp.getCreatedDate())
                        .lastModifiedDate(dmp.getLastModifiedDate())
                        .batch(
                                BatchBasicResponse.builder()
                                        .id(dmp.getBatchId())
                                        .courseId(dmp.getCourseId())
                                        .name(dmp.getBatchName())
                                        .status(dmp.getBatchStatus())
                                        .enrollmentStatus(dmp.getBatchEnrollmentStatus())
                                        .createdDate(dmp.getBatchCreatedDate())
                                        .lastModifiedDate(dmp.getBatchLastModifiedDate())
                                        .build()
                        )
                        .teacher(
                                TeacherBasicResponse.builder()
                                        .id(dmp.getTeacherId())
                                        .firstName(dmp.getTeacherFirstName())
                                        .lastName(dmp.getTeacherLastName())
                                        .email(dmp.getUserEmail())
                                        .contact(dmp.getUserContact())
                                        .dp(dmp.getUserDp())
                                        .userslug(dmp.getUserSlug())
                                        .build()
                        )
                        .build()
                )
                .orElseThrow(() -> new NotFoundException("Module with id " + id + " not found"));
    }

    @Override
    public ModuleResponse getModuleById(Long id) {
        return moduleRepository.findById(id)
                .map(this::mapToAnnouncementResponse)
                .orElseThrow(() -> new NotFoundException("Module with id " + id + " not found"));
    }

    private ModuleEntity updateStatus(ModuleEntity moduleEntity, ModuleStatus status) {

        if (!isModuleOwner(moduleEntity)) {
            throw new InvalidInputException("You are not authorized to update this module");
        }
        moduleEntity.setStatus(status);
        return moduleRepository.save(moduleEntity);
    }

    @Override
    public ModuleEntity getModuleEntityById(Long id){
        return moduleRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("Module with id " + id + " not found"));
    }

    @Override
    public PaginatedApiResponse<ModuleAssignmentResponse> getAssignmentsByModule(Long id, int page, int size, String direction, List<String> sortBy, ModuleAssignmentFilterRequest filters) {
        Pageable pageable = PageRequest.of(page, size, SortUtil.buildSort(direction, sortBy, List.of("created_date")));
        return moduleAssignmentQueryService.getAssignmentsByModule(id, filters, pageable);
    }

    private boolean isModuleOwner(ModuleEntity entity){
       return entity.getBatch().getCourse().getInstitute().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private boolean isModuleExists(String name, Long batchId){
        return moduleRepository.existsByNameAndBatchId(name,batchId);
    }

    private ModuleStatus mapToModuleStatus(ModuleCreateStatus status){
        switch (status){
            case DRAFT -> {
                return ModuleStatus.DRAFT;
            }
            case PUBLISHED -> {
                return ModuleStatus.PUBLISHED;
            }
            case LOCKED -> {
                return ModuleStatus.LOCKED;
            }
            default -> throw new IllegalArgumentException("Invalid module status: " + status);
        }
    }

    private ModuleResponse mapToAnnouncementResponse(ModuleEntity entity){
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
