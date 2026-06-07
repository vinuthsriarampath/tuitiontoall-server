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


import edu.vinu.entity.TeacherVacancyEntity;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.enums.TeacherVacancyStatus;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.model.TeacherVacancy;
import edu.vinu.domain.institute.repository.InstituteRepository;
import edu.vinu.repository.TeacherVacancyRepository;
import edu.vinu.request.CreateVacancyRequest;
import edu.vinu.request.UpdateVacancyRequest;
import edu.vinu.service.common.TeacherVacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherVacancyServiceImpl implements TeacherVacancyService {
    private final TeacherVacancyRepository vacancyRepository;
    private final InstituteRepository instituteRepository;

    @Override
    public TeacherVacancy createVacancy(CreateVacancyRequest request) {

        InstituteEntity institute = instituteRepository.findInstituteByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NotFoundException("Institute not found"));

        TeacherVacancyEntity vacancy = TeacherVacancyEntity.builder()
                .title(request.getTitle())
                .requiredExperienceYears(request.getRequiredExperienceYears())
                .jobDescription(request.getJobDescription())
                .status(TeacherVacancyStatus.OPEN)
                .vacancyClosingDate(request.getVacancyClosingDate())
                .institute(institute)
                .build();

        return mapToDTO(vacancyRepository.save(vacancy));
    }

    @Override
    public TeacherVacancy updateVacancy(Long vacancyId, UpdateVacancyRequest request) {

        TeacherVacancyEntity vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new NotFoundException("Vacancy not found"));

        if (request.getTitle() != null) vacancy.setTitle(request.getTitle());
        if (request.getRequiredExperienceYears() != null) vacancy.setRequiredExperienceYears(request.getRequiredExperienceYears());
        if (request.getJobDescription() != null) vacancy.setJobDescription(request.getJobDescription());
        if (request.getStatus() != null) vacancy.setStatus(request.getStatus());
        if (request.getVacancyClosingDate() != null) vacancy.setVacancyClosingDate(request.getVacancyClosingDate());

        vacancy.setLastModifiedDate(LocalDateTime.now());

        return mapToDTO(vacancyRepository.save(vacancy));
    }

    @Override
    public void deleteVacancy(Long vacancyId) {
        vacancyRepository.deleteById(vacancyId);
    }

    @Override
    public TeacherVacancy getById(Long vacancyId) {
        return mapToDTO(this.getEntityById(vacancyId));
    }

    @Override
    public Page<TeacherVacancy> getAllByInstitute(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        InstituteEntity institute = instituteRepository.findInstituteByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NotFoundException("Institute not found"));
        return vacancyRepository.findByInstituteId(institute.getId(), pageable).map(this::mapToDTO);
    }

    @Override
    public List<TeacherVacancy> getAllByInstituteIdAndStatus(Long instituteId, String status) {
        return vacancyRepository.findByStatusAndInstituteId(TeacherVacancyStatus.valueOf(status),instituteId)
                .stream().map(this::mapToDTO).toList();
    }

    @Override
    public TeacherVacancyEntity getEntityById(Long id) {
        return vacancyRepository.findById(id).orElseThrow(() -> new NotFoundException("Vacancy not found"));
    }

    @Override
    public boolean existsById(Long id) {
        return vacancyRepository.existsById(id);
    }

    @Override
    public boolean isVacancyOpened(Long id) {
        return getEntityById(id).getStatus().equals(TeacherVacancyStatus.OPEN);
    }

    @Override
    public TeacherVacancy getByVacancyIdAndStatus(Long vacancyId, TeacherVacancyStatus status) {
        this.vacancyRepository.findByIdAndStatus(vacancyId,status).orElseThrow(() -> new NotFoundException("Vacancy not found with the given Id/Status"));
        return mapToDTO(this.getEntityById(vacancyId));
    }

    private TeacherVacancy mapToDTO(TeacherVacancyEntity v) {
        return TeacherVacancy.builder()
                .id(v.getId())
                .title(v.getTitle())
                .requiredExperienceYears(v.getRequiredExperienceYears())
                .jobDescription(v.getJobDescription())
                .status(v.getStatus())
                .vacancyClosingDate(v.getVacancyClosingDate())
                .instituteId(v.getInstitute().getId())
                .build();
    }
}
