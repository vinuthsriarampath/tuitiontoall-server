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
import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.enums.AnnouncementStatus;
import edu.vinu.enums.AnnouncementVisibility;
import edu.vinu.exception.custom.BadRequestException;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.exception.custom.UnauthorizedException;
import edu.vinu.repository.AnnouncementRepository;
import edu.vinu.request.announcements.AnnouncementCreateRequest;
import edu.vinu.request.announcements.AnnouncementVisibilityUpdateRequest;
import edu.vinu.request.announcements.enums.AnnouncementCreateStatus;
import edu.vinu.response.AnnouncementResponse;
import edu.vinu.response.FieldError;
import edu.vinu.service.auth.UserAuthenticationService;
import edu.vinu.service.common.AnnouncementService;
import edu.vinu.service.common.BatchService;
import edu.vinu.service.common.CourseService;
import edu.vinu.service.common.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    @Override
    public AnnouncementResponse createAnnouncement(AnnouncementCreateRequest request) {

        validateExpireAt(request.getExpireAt());
        validateVisibilityRules(request.getVisibility(), request.getCourseId(), request.getBatchId());

        UserEntity userEntity = userService.getUserEntityByEmail(userAuthenticationService.getCurrentUserEmail());
        InstituteEntity instituteEntity = userEntity.getInstitute();

        if (instituteEntity == null) {
            throw new UnauthorizedException("You are not associated with any institute.");
        }

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
        AnnouncementEntity announcementEntity = announcementRepository.findById(announcementId).orElseThrow(() -> new NotFoundException("Announcement not found with id: " + announcementId));

        if (!isOwner(announcementEntity)) {
            throw new UnauthorizedException("You are not authorized to update this announcement.");
        }

        validateVisibilityChange(announcementEntity,request.getVisibility(),request.getCourseId(), request.getBatchId());

        validateVisibilityRules(request.getVisibility(), request.getCourseId(), request.getBatchId());

        applyVisibility(announcementEntity, request.getVisibility(), request.getCourseId(), request.getBatchId(), announcementEntity.getInstitute());
        return mapToAnnouncementResponse(announcementRepository.save(announcementEntity));
    }

    private void applyVisibility(AnnouncementEntity announcement, AnnouncementVisibility visibility, Long courseId, Long batchId, InstituteEntity institute) {

        announcement.setInstitute(institute);

        switch (visibility) {

            case PRIVATE -> {
                announcement.setVisibility(AnnouncementVisibility.PRIVATE);
                announcement.setCourse(null);
                announcement.setBatch(null);
            }

            case All_TEACHERS -> {
                announcement.setVisibility(AnnouncementVisibility.All_TEACHERS);
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

        if (visibility == AnnouncementVisibility.All_TEACHERS) {
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

            case PRIVATE, All_TEACHERS -> throw new InvalidInputException(
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
