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
import edu.vinu.enums.ModuleStatus;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.repository.ModuleRepository;
import edu.vinu.request.modules.ModuleCreateRequest;
import edu.vinu.request.modules.ModuleNameUpdateRequest;
import edu.vinu.request.modules.enums.ModuleCreateStatus;
import edu.vinu.response.FieldError;
import edu.vinu.response.module.ModuleResponse;
import edu.vinu.service.common.BatchService;
import edu.vinu.service.common.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;
    private final BatchService batchService;
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

        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }

        ModuleEntity moduleEntity = ModuleEntity.builder()
                .name(request.getName())
                .batch(batchEntity)
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

    private ModuleEntity getModuleEntityById(Long id){
        return moduleRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("Module with id " + id + " not found"));
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
                .createdDate(entity.getCreatedAt())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }
}
