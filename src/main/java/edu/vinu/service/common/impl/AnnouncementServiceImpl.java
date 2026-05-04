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

import edu.vinu.entity.AnnouncementEntity;
import edu.vinu.entity.BatchEntity;
import edu.vinu.entity.CourseEntity;
import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.enums.AnnouncementStatus;
import edu.vinu.enums.AnnouncementVisibility;
import edu.vinu.exception.custom.BadRequestException;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.exception.custom.UnauthorizedException;
import edu.vinu.repository.AnnouncementRepository;
import edu.vinu.request.announcements.AnnouncementCreateRequest;
import edu.vinu.request.announcements.AnnouncementFilterRequest;
import edu.vinu.request.announcements.AnnouncementUpdateRequest;
import edu.vinu.request.announcements.AnnouncementVisibilityUpdateRequest;
import edu.vinu.request.announcements.enums.AnnouncementCreateStatus;
import edu.vinu.response.AnnouncementResponse;
import edu.vinu.response.FieldError;
import edu.vinu.service.auth.UserAuthenticationService;
import edu.vinu.service.common.*;
import edu.vinu.util.SortUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserService userService;
    private final CourseService courseService;
    private final BatchService batchService;
    private final UserAuthenticationService userAuthenticationService;
    private final InstituteService instituteService;

    @Override
    public AnnouncementResponse createAnnouncement(AnnouncementCreateRequest request) {

        validateExpireAt(request.getExpireAt());
        validateVisibilityRules(request.getVisibility(), request.getCourseId(), request.getBatchId());

        InstituteEntity instituteEntity = instituteService.getCurrentInstitute();

        AnnouncementStatus status = mapStatus(request.getStatus());

        AnnouncementEntity announcementEntity = AnnouncementEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .visibility(request.getVisibility())
                .status(status)
                .isPinned(false)
                .publishedDate(status == AnnouncementStatus.PUBLISHED ? LocalDateTime.now() : null)
                .expireAt(request.getExpireAt())
                .build();

        applyVisibility(announcementEntity, request.getVisibility(), request.getCourseId(), request.getBatchId(), instituteEntity);

        return mapToAnnouncementResponse(announcementRepository.save(announcementEntity));

    }

    @Override
    @Transactional
    public AnnouncementResponse updateAnnouncementVisibility(Long announcementId, AnnouncementVisibilityUpdateRequest request) {
        AnnouncementEntity announcementEntity = this.getAnnouncementEntityById(announcementId);
        if (!isOwner(announcementEntity)) {
            throw new UnauthorizedException("You are not authorized to update this announcement.");
        }

        validateVisibilityChange(announcementEntity,request.getVisibility(),request.getCourseId(), request.getBatchId());

        validateVisibilityRules(request.getVisibility(), request.getCourseId(), request.getBatchId());

        applyVisibility(announcementEntity, request.getVisibility(), request.getCourseId(), request.getBatchId(), announcementEntity.getInstitute());
        return mapToAnnouncementResponse(announcementRepository.save(announcementEntity));
    }

    @Override
    public Page<AnnouncementResponse> getAllAnnouncements(int page, int size,String direction, List<String> sortBy, AnnouncementFilterRequest filters) {

        Pageable pageable = PageRequest.of(page,size, SortUtil.buildSort(direction,sortBy,List.of("published_date")));

        InstituteEntity instituteEntity = instituteService.getCurrentInstitute();

        return announcementRepository.findAllByInstituteWithFilters(instituteEntity.getId(),filters.visibility(), filters.status(), filters.courseId(), filters.batchId(),pageable).map(this::mapToAnnouncementResponse);
    }

    @Override
    @Transactional
    public AnnouncementResponse updateAnnouncementTitleAndDescription(Long announcementId, AnnouncementUpdateRequest request) {
        AnnouncementEntity announcementEntity = this.getAnnouncementEntityById(announcementId);

        if (!isOwner(announcementEntity)) {
            throw new UnauthorizedException("You are not authorized to update this announcement.");
        }

        List<FieldError> errors = new ArrayList<>();

        if(!request.title().isBlank()){
            announcementEntity.setTitle(request.title());
        }else {
            errors.add(new FieldError("title","title is required!"));
        }

        if(!request.description().isBlank()){
            announcementEntity.setDescription(request.description());
        }else {
            errors.add(new FieldError("title", "Title must not be blank"));
        }

        if(!errors.isEmpty()){
            throw new InvalidInputException(errors);
        }

        return mapToAnnouncementResponse(announcementRepository.save(announcementEntity));
    }

    @Override
    @Transactional
    public AnnouncementResponse archiveAnnouncementById(Long id) {
        AnnouncementEntity announcementEntity = this.getAnnouncementEntityById(id);
        if (!isOwner(announcementEntity)) {
            throw new UnauthorizedException("You are not authorized to archive this announcement.");
        }

        announcementEntity.setStatus(AnnouncementStatus.ARCHIVED);
        announcementEntity.setPinned(false);

        return mapToAnnouncementResponse(announcementRepository.save(announcementEntity));
    }

    @Override
    @Transactional
    public AnnouncementResponse pinAnnouncementById(Long id) {
        AnnouncementEntity announcementEntity = this.getAnnouncementEntityById(id);
        if (!isOwner(announcementEntity)) {
            throw new UnauthorizedException("You are not authorized to archive this announcement.");
        }
        isAnnouncementPinnable(announcementEntity);
        announcementEntity.setPinned(true);
        return mapToAnnouncementResponse(announcementRepository.save(announcementEntity));
    }

    @Override
    @Transactional
    public AnnouncementResponse unpinAnnouncementById(Long id) {
        AnnouncementEntity announcementEntity = this.getAnnouncementEntityById(id);
        if (!isOwner(announcementEntity)) {
            throw new UnauthorizedException("You are not authorized to archive this announcement.");
        }
        announcementEntity.setPinned(false);
        return mapToAnnouncementResponse(announcementRepository.save(announcementEntity));
    }

    private void isAnnouncementPinnable(AnnouncementEntity announcementEntity) {
        final int MAX_PINNED_ANNOUNCEMENTS = 3;
        final String BASE_MAX_PINNED_ERROR = "Have Reached Max Pinned Announcements";

        if (announcementEntity.getStatus().equals(AnnouncementStatus.ARCHIVED) || announcementEntity.getStatus().equals(AnnouncementStatus.DRAFT) || announcementEntity.getStatus().equals(AnnouncementStatus.DELETED) ) {
            throw new InvalidInputException("Announcements with status ARCHIVED, DRAFT or DELETED cannot be pinned");
        }

        switch (announcementEntity.getVisibility()){
            case PRIVATE -> {
                throw new InvalidInputException("Announcements with visibility PRIVATE cannot be pinned");
            }
            case ALL_TEACHERS -> {
                int i = announcementRepository.countAnnouncementsByInstitute(announcementEntity.getInstitute().getId(), AnnouncementStatus.PUBLISHED.name(), AnnouncementVisibility.ALL_TEACHERS.name(), true, null, null);

                if(i >= MAX_PINNED_ANNOUNCEMENTS){
                    throw new InvalidInputException(BASE_MAX_PINNED_ERROR+" for all teachers!.");
                }
            }
            case COURSE -> {
                int i = announcementRepository.countAnnouncementsByInstitute(announcementEntity.getInstitute().getId(), AnnouncementStatus.PUBLISHED.name(), AnnouncementVisibility.COURSE.name(), true, announcementEntity.getCourse().getId(), null);

                if(i >= MAX_PINNED_ANNOUNCEMENTS){
                    throw new InvalidInputException(BASE_MAX_PINNED_ERROR+" for course id: "+announcementEntity.getCourse().getId());
                }
            }
            case BATCH -> {
                int i = announcementRepository.countAnnouncementsByInstitute(announcementEntity.getInstitute().getId(),AnnouncementStatus.PUBLISHED.name(),AnnouncementVisibility.BATCH.name(),true,announcementEntity.getCourse().getId(),announcementEntity.getBatch().getId());

                if(i >= MAX_PINNED_ANNOUNCEMENTS){
                    throw new InvalidInputException(BASE_MAX_PINNED_ERROR+" for batch id: "+announcementEntity.getBatch().getId());
                }
            }
        }

    }

    private AnnouncementEntity getAnnouncementEntityById(Long announcementId) {
        return announcementRepository.findById(announcementId).orElseThrow(() -> new NotFoundException("Announcement not found with id: " + announcementId));
    }

    private void applyVisibility(AnnouncementEntity announcement, AnnouncementVisibility visibility, Long courseId, Long batchId, InstituteEntity institute) {

        announcement.setInstitute(institute);

        switch (visibility) {

            case PRIVATE -> {
                announcement.setVisibility(AnnouncementVisibility.PRIVATE);
                announcement.setCourse(null);
                announcement.setBatch(null);
            }

            case ALL_TEACHERS -> {
                announcement.setVisibility(AnnouncementVisibility.ALL_TEACHERS);
                announcement.setCourse(null);
                announcement.setBatch(null);
            }

            case COURSE -> {
                CourseEntity course = courseService.getCourseEntityById(courseId);
                validateCourse(course);

                announcement.setVisibility(AnnouncementVisibility.COURSE);
                announcement.setCourse(course);
                announcement.setBatch(null);
            }

            case BATCH -> {
                CourseEntity course = courseService.getCourseEntityById(courseId);
                BatchEntity batch = batchService.getBatchEntityById(batchId);

                validateCourse(course);
                validateBatch(batch, course);

                announcement.setVisibility(AnnouncementVisibility.BATCH);
                announcement.setCourse(course);
                announcement.setBatch(batch);
            }

            default -> throw new InvalidInputException("visibility", "Unsupported visibility type");
        }
    }

    private boolean isOwner(AnnouncementEntity entity) {
        String currentUserEmail = userAuthenticationService.getCurrentUserEmail();
        return entity.getInstitute() != null && entity.getInstitute().getUser() != null && currentUserEmail.equals(entity.getInstitute().getUser().getEmail());
    }

    private AnnouncementResponse mapToAnnouncementResponse(AnnouncementEntity entity) {
        if (entity == null) {
            throw new BadRequestException("Entity is null.");
        }
        return AnnouncementResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .visibility(entity.getVisibility())
                .status(entity.getStatus())
                .isPinned(entity.isPinned())
                .publishedDate(entity.getPublishedDate())
                .expireAt(entity.getExpireAt())
                .instituteId(entity.getInstitute().getId())
                .courseId(entity.getCourse() != null ? entity.getCourse().getId() : null)
                .batchId(entity.getBatch() != null ? entity.getBatch().getId() : null)
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }

    private void validateExpireAt(LocalDateTime expireAt) {
        if (expireAt == null) {
            throw new InvalidInputException("expireAt", "Expire date is required");
        }
        if (expireAt.isBefore(LocalDateTime.now())) {
            throw new InvalidInputException("expireAt", "Expire date must be in the future");
        }
    }


    private void validateCourse(CourseEntity courseEntity) {
        if (courseEntity == null) {
            throw new InvalidInputException("courseId", "Course not found");
        }
        if (!courseService.isCourseOwner(courseEntity)) {
            throw new UnauthorizedException("You are not authorized to access this course.");
        }
    }

    private void validateBatch(BatchEntity batchEntity, CourseEntity course) {
        if (batchEntity == null) {
            throw new InvalidInputException("batchId", "Batch not found");
        }
        if (!batchService.isBatchOwner(batchEntity)) {
            throw new UnauthorizedException("You are not authorized to access this batch.");
        }
        if (!batchService.isBatchBelongToCourse(batchEntity, course.getId())) {
            throw new InvalidInputException("batchId", "Batch does not belong to the specified course");
        }
    }

    private void validateVisibilityRules(AnnouncementVisibility visibility, Long courseId, Long batchId) {
        List<FieldError> errors = new ArrayList<>();

        if (visibility == AnnouncementVisibility.PRIVATE) {
            if (courseId != null) errors.add(new FieldError("courseId", "Must not be provided for PRIVATE"));
            if (batchId != null) errors.add(new FieldError("batchId", "Must not be provided for PRIVATE"));
        }

        if (visibility == AnnouncementVisibility.ALL_TEACHERS) {
            if (courseId != null) errors.add(new FieldError("courseId", "Must not be provided for All_TEACHERS"));
            if (batchId != null) errors.add(new FieldError("batchId", "Must not be provided for All_TEACHERS"));
        }

        if (visibility == AnnouncementVisibility.COURSE) {
            if (courseId == null) errors.add(new FieldError("courseId", "Course id must be provided for COURSE"));
            if (batchId != null) errors.add(new FieldError("batchId", "Must not be provided for COURSE"));
        }

        if (visibility == AnnouncementVisibility.BATCH) {
            if (courseId == null) errors.add(new FieldError("courseId", "Course id must be provided for BATCH"));
            if (batchId == null) errors.add(new FieldError("batchId", "Batch id must be provided"));
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }
    }

    private void validateVisibilityChange(AnnouncementEntity entity, AnnouncementVisibility requestVisibility, Long requestCourseId, Long requestBatchId) {
        if (entity.getVisibility() != requestVisibility) return;

        switch (requestVisibility) {

            case PRIVATE, ALL_TEACHERS -> throw new InvalidInputException(
                    "visibility",
                    "Announcement already has the specified visibility"
            );

            case COURSE -> {
                if (entity.getCourse() != null &&
                        entity.getCourse().getId().equals(requestCourseId)) {
                    throw new InvalidInputException(
                            "visibility",
                            "Announcement already has the specified visibility with given course"
                    );
                }
            }

            case BATCH -> {
                if (entity.getCourse() != null &&
                        entity.getBatch() != null &&
                        entity.getCourse().getId().equals(requestCourseId) &&
                        entity.getBatch().getId().equals(requestBatchId)) {
                    throw new InvalidInputException(
                            "visibility",
                            "Announcement already has the specified visibility with given course and batch"
                    );
                }
            }
        }
    }

    private AnnouncementStatus mapStatus(AnnouncementCreateStatus status) {
        return switch (status) {
            case PUBLISHED -> AnnouncementStatus.PUBLISHED;
            case DRAFT -> AnnouncementStatus.DRAFT;
        };
    }

}
