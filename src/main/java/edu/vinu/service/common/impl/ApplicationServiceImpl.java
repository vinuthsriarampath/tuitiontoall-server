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

import edu.vinu.entity.ApplicationEntity;
import edu.vinu.entity.TeacherVacancyEntity;
import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.enums.ApplicationStatus;
import edu.vinu.enums.TeacherVacancyStatus;
import edu.vinu.exception.custom.BadRequestException;
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.exception.custom.UnauthorizedException;
import edu.vinu.model.Application;
import edu.vinu.repository.ApplicationRepository;
import edu.vinu.response.ApplicationDetailsResponse;
import edu.vinu.response.TeacherUserResponse;
import edu.vinu.service.common.ApplicationService;
import edu.vinu.service.common.EmailService;
import edu.vinu.service.common.TeacherVacancyService;
import edu.vinu.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final UserService userService;
    private final ApplicationRepository applicationRepository;
    private final TeacherVacancyService teacherVacancyService;
    private final EmailService emailService;

    @Override
    public Application createApplication(Long vacancyId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.getUserEntityByEmail(email);
        if (userEntity.getRole().getRole().equals("teacher")) {

            TeacherVacancyEntity teacherVacancyEntity = teacherVacancyService.getEntityById(vacancyId);

            if (teacherVacancyEntity == null) {
                throw new NotFoundException("Vacancy not found");
            }

            if (teacherVacancyEntity.getStatus() == TeacherVacancyStatus.OPEN) {
                if (this.isUserAlreadyApplied(userEntity.getTeacher().getId(),vacancyId)) {
                    throw new BadRequestException("You have already applied for this vacancy");
                }
                ApplicationEntity applicationEntity =  ApplicationEntity.builder()
                        .teacherVacancy(teacherVacancyEntity)
                        .teacher(userEntity.getTeacher())
                        .status(ApplicationStatus.PENDING)
                        .build();
                ApplicationEntity save = applicationRepository.save(applicationEntity);

                emailService.SendApplicationSuccessEmail(userEntity.getEmail(), userEntity.getTeacher().getFirstName()+" "+userEntity.getTeacher().getLastName(),"#");
                return this.mapToDto(save);
            }else{
                throw new BadRequestException("Vacancy is not open for applications");
            }
        } else {
            throw new UnauthorizedException("Only teachers can apply for vacancies");
        }

    }

    @Override
    public boolean isUserAlreadyApplied(Long teacherId,Long vacancyId) {
        if(!teacherVacancyService.existsById(vacancyId)){
            throw new NotFoundException("Vacancy not found!");
        }
        if (!userService.isTeacherExistByTeacherId(teacherId)) {
            throw new NotFoundException("Teacher not found!");
        }
        return applicationRepository.isUserAlreadyApplied(teacherId,vacancyId) == 1;
    }

    @Override
    public Page<ApplicationDetailsResponse> getApplicationsByVacancy(Long vacancyId, int page, int size, String direction, String sortBy) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        if (teacherVacancyService.existsById(vacancyId)){
            return applicationRepository.findAllByTeacherVacancy_Id(vacancyId, pageable).map(ap -> ApplicationDetailsResponse.builder()
                    .id(ap.getId())
                    .teacherVacancyId(ap.getTeacherVacancyId())
                    .status(ApplicationStatus.valueOf(ap.getStatus()))
                    .appliedDate(ap.getAppliedDate())
                    .lastModifiedDate(ap.getLastModifiedDate())
                    .teacher(
                            TeacherUserResponse.builder()
                                    .userId(ap.getUserId())
                                    .email(ap.getEmail())
                                    .contact(ap.getContact())
                                    .dp(ap.getDp())
                                    .address(ap.getAddress())
                                    .firstName(ap.getFirstName())
                                    .lastName(ap.getLastName())
                                    .dob(ap.getDob())
                                    .build()
                    )
                    .build());
        }else {
            throw new NotFoundException("Vacancy not found!");
        }
    }

    @Override
    public List<ApplicationEntity> getAllApplicationEntitiesByIds(List<Long> applicationIds) {
        return applicationRepository.findAllById(applicationIds);
    }

    @Override
    public void setApplicationStatusSelected(ApplicationEntity applicationEntity) {
        applicationEntity.setStatus(ApplicationStatus.SELECTED);
        applicationRepository.save(applicationEntity);
    }

    @Override
    public void setApplicationStatusRejected(ApplicationEntity applicationEntity) {
        applicationEntity.setStatus(ApplicationStatus.REJECTED);
        applicationRepository.save(applicationEntity);
    }

    private Application mapToDto(ApplicationEntity a){
        return  Application.builder()
                .id(a.getId())
                .teacherId(a.getTeacher().getId())
                .status(a.getStatus())
                .teacherVacancyId(a.getTeacherVacancy().getId())
                .appliedDate(a.getAppliedDate())
                .last_modified_date(a.getLastModifiedDate())
                .build();
    }
}
