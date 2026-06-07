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

import edu.vinu.domain.user.response.TeacherBasicResponse;
import edu.vinu.domain.user.response.TeacherUserResponse;
import edu.vinu.entity.ApplicationEntity;
import edu.vinu.domain.institute.entity.InstituteTeacherEntity;
import edu.vinu.domain.user.entity.TeacherEntity;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.enums.ApplicationStatus;
import edu.vinu.enums.InstituteTeacherStatus;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.repository.InstituteTeacherRepository;
import edu.vinu.repository.projection.InstituteTeacherStatsProjection;
import edu.vinu.request.ApplicationRejectionRequest;
import edu.vinu.request.ApplicationSelectionRequest;
import edu.vinu.response.*;
import edu.vinu.service.common.ApplicationService;
import edu.vinu.service.common.InstituteService;
import edu.vinu.service.common.InstituteTeacherService;
import edu.vinu.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstituteTeacherServiceImpl implements InstituteTeacherService {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final InstituteTeacherRepository instituteTeacherRepository;
    private final InstituteService instituteService;

    @Override
    @Transactional
    public ApplicationSelectionResponse onBoardTeachers(ApplicationSelectionRequest request) {

        List<Long> successIds = new ArrayList<>();
        List<Long> failedIds = new ArrayList<>();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.getUserEntityByEmail(email);

        Long instituteId = userEntity.getInstitute().getId();

        List<ApplicationEntity> applicationEntities = applicationService.getAllApplicationEntitiesByIds(request.getApplicationIds());
        Map<Long, ApplicationEntity> applicationEntityMap = applicationEntities
                .stream()
                .collect(
                        Collectors.toMap(ApplicationEntity::getId, applicationEntity -> applicationEntity)
                );

        for (Long applicationId : request.getApplicationIds()){
            ApplicationEntity applicationEntity = applicationEntityMap.get(applicationId);

            if (applicationEntity == null){
                failedIds.add(applicationId);
                continue;
            }

            try{
                Long teacherId = applicationEntity.getTeacher().getId();
                boolean exists = instituteTeacherRepository.existsByTeacherIdAndInstituteId(teacherId,instituteId);

                if (exists) {
                    failedIds.add(applicationId);
                    continue;
                }

                InstituteTeacherEntity entity = InstituteTeacherEntity.builder()
                        .teacher(applicationEntity.getTeacher())
                        .institute(userEntity.getInstitute())
                        .status(InstituteTeacherStatus.ACTIVE)
                        .build();

                instituteTeacherRepository.save(entity);

                applicationEntity.setStatus(ApplicationStatus.SELECTED);

                applicationService.setApplicationStatusSelected(applicationEntity);

                successIds.add(applicationId);

            }catch (Exception ex){
                failedIds.add(applicationId);
            }
        }

        return ApplicationSelectionResponse.builder()
                .successApplicationIds(successIds)
                .failedApplicationIds(failedIds)
                .build();
    }

    @Override
    public ApplicationRejectionResponse rejectApplications(ApplicationRejectionRequest request) {
        ArrayList<Long> successIds = new ArrayList<>();
        ArrayList<Long> failedIds = new ArrayList<>();

        UserEntity userEntity = userService.getUserEntityByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Long instituteId = userEntity.getInstitute().getId();

        List<ApplicationEntity> applicationEntities = applicationService.getAllApplicationEntitiesByIds(request.getApplicationIds());

        Map<Long, ApplicationEntity> applicationEntityMap =  applicationEntities
                .stream()
                .collect(
                        Collectors.toMap(ApplicationEntity::getId, applicationEntity -> applicationEntity)
                );

        for (Long applicationId : request.getApplicationIds()){
            ApplicationEntity applicationEntity = applicationEntityMap.get(applicationId);

            if (applicationEntity == null){
                failedIds.add(applicationId);
                continue;
            }

            try{
                Long teacherId = applicationEntity.getTeacher().getId();
                boolean exists = instituteTeacherRepository.existsByTeacherIdAndInstituteId(teacherId,instituteId);

                if (exists) {
                    failedIds.add(applicationId);
                    continue;
                }

                applicationService.setApplicationStatusRejected(applicationEntity);
                successIds.add(applicationId);

            }catch (Exception ex){
                failedIds.add(applicationId);
            }
        }
        return ApplicationRejectionResponse.builder()
                .successApplicationIds(successIds)
                .failedApplicationIds(failedIds)
                .build();
    }

    @Override
    public Page<InstituteTeacherResponse> getAllTeachersByInstitute(int page, int size, String direction, String sortBy) {
        UserEntity user = userService.getUserEntityByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Long instituteId = user.getInstitute().getId();

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection,sortBy));

        return instituteTeacherRepository.getAllByInstituteId(instituteId,pageable).map(itp -> InstituteTeacherResponse.builder()
                .id(itp.getId())
                .status(itp.getStatus())
                .instituteId(itp.getInstituteId())
                .joinedDate(itp.getJoinedDate())
                .lastModifiedDate(itp.getLastModifiedDate())
                .teacher(
                        TeacherUserResponse.builder()
                                .userId(itp.getUserId())
                                .email(itp.getEmail())
                                .contact(itp.getContact())
                                .dp(itp.getDp())
                                .address(itp.getAddress())
                                .firstName(itp.getFirstName())
                                .lastName(itp.getLastName())
                                .dob(itp.getDob())
                                .build()
                )
                .build());
    }

    @Override
    public InstituteTeacherStatsResponse getInstituteTeacherStats() {
        UserEntity user = userService.getUserEntityByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Long instituteId = user.getInstitute().getId();

        InstituteTeacherStatsProjection projection = instituteTeacherRepository.getInstituteTeacherStatsByInstituteId(instituteId);

        return InstituteTeacherStatsResponse.builder()
                .totalTeachers(projection.getTotalTeachers())
                .activeTeachers(projection.getActiveTeachers())
                .inactiveTeachers(projection.getInactiveTeachers())
                .suspendedTeachers(projection.getSuspendedTeachers())
                .build();
    }

    @Override
    public List<TeacherBasicResponse> getAllTeachersByCurrentInstitute() {

        Long instituteId = instituteService.getCurrentInstitute().getId();

        return instituteTeacherRepository.findAllTeachersByInstituteId(instituteId).stream().map(tp -> TeacherBasicResponse.builder()
                .id(tp.getTeacherId())
                .firstName(tp.getFirstName())
                .lastName(tp.getLastName())
                .build()
        ).toList();
    }

    @Override
    public TeacherEntity getCurrentInstituteRelatedTeacherEntityById(Long id) {
        Long instituteId = instituteService.getCurrentInstitute().getId();
        InstituteTeacherEntity entity =instituteTeacherRepository.findByTeacherIdAndInstituteId(id, instituteId).orElseThrow(() -> new NotFoundException("Teacher with id " + id + " not found in the current institute"));
        return entity.getTeacher();
    }


}
