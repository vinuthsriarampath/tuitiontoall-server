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
import edu.vinu.exception.custom.UnauthorizedException;
import edu.vinu.repository.AnnouncementRepository;
import edu.vinu.request.announcements.AnnouncementCreateRequest;
import edu.vinu.request.announcements.enums.AnnouncementCreateStatus;
import edu.vinu.response.AnnouncementResponse;
import edu.vinu.response.FieldError;
import edu.vinu.service.common.AnnouncementService;
import edu.vinu.service.common.BatchService;
import edu.vinu.service.common.CourseService;
import edu.vinu.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public AnnouncementResponse createAnnouncement(AnnouncementCreateRequest request) {

        validateAnnouncement(request);

        UserEntity userEntity = userService.getUserEntityByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        InstituteEntity instituteEntity = userEntity.getInstitute();

        if (instituteEntity == null) {
            throw new UnauthorizedException("You are not associated with any institute.");
        }

        AnnouncementEntity announcementEntity = AnnouncementEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .visibility(request.getVisibility())
                .status(mapStatus(request.getStatus()))
                .isPinned(false)
                .publishedDate(request.getStatus().equals(AnnouncementCreateStatus.PUBLISHED) ? LocalDateTime.now() : null)
                .expireAt(request.getExpireAt())
                .build();

        switch (request.getVisibility()) {
            case All_TEACHERS -> announcementEntity.setInstitute(instituteEntity);
            case COURSE -> {
                CourseEntity courseEntity = courseService.getCourseEntityById(request.getCourseId());

                validateCourse(courseEntity);

                announcementEntity.setInstitute(instituteEntity);
                announcementEntity.setCourse(courseEntity);
            }
            case BATCH -> {
                CourseEntity courseEntity = courseService.getCourseEntityById(request.getCourseId());
                BatchEntity batchEntity = batchService.getBatchEntityById(request.getBatchId());

                validateCourse(courseEntity);
                validateBatch(batchEntity, request.getCourseId());

                announcementEntity.setInstitute(instituteEntity);
                announcementEntity.setCourse(courseEntity);
                announcementEntity.setBatch(batchEntity);

            }
        }
        return mapToAnnouncementResponse(announcementRepository.save(announcementEntity));

    }


    private void validateCourse(CourseEntity courseEntity) {
        if (courseEntity == null) {
            throw new InvalidInputException("courseId", "Course not found");
        }
        if (!courseService.isCourseOwner(courseEntity)) {
            throw new UnauthorizedException("You are not authorized to access this course.");
        }
    }

    private void validateBatch(BatchEntity batchEntity, Long courseId) {
        if (batchEntity == null) {
            throw new InvalidInputException("batchId", "Batch not found");
        }
        if (!batchService.isBatchOwner(batchEntity)) {
            throw new UnauthorizedException("You are not authorized to access this batch.");
        }
        if (!batchService.isBatchBelongToCourse(batchEntity, courseId)) {
            throw new InvalidInputException("batchId", "Batch does not belong to the specified course");
        }
    }

    private void validateAnnouncement(AnnouncementCreateRequest request) {

        List<FieldError> errors = new ArrayList<>();

        // Visibility rules
        if (request.getVisibility() == AnnouncementVisibility.All_TEACHERS) {
            if (request.getCourseId() != null) {
                errors.add(new FieldError("courseId", "Must not be provided for All_TEACHERS"));
            }
            if (request.getBatchId() != null) {
                errors.add(new FieldError("batchId", "Must not be provided for All_TEACHERS"));
            }
        }

        if (request.getVisibility() == AnnouncementVisibility.COURSE) {
            if (request.getCourseId() == null) {
                errors.add(new FieldError("courseId", "Course id must be provided"));
            }
            if (request.getBatchId() != null) {
                errors.add(new FieldError("batchId", "Must not be provided for COURSE"));
            }
        }

        if (request.getVisibility() == AnnouncementVisibility.BATCH) {
            if (request.getCourseId() == null) {
                errors.add(new FieldError("courseId", "Course id must be provided"));
            }
            if (request.getBatchId() == null) {
                errors.add(new FieldError("batchId", "Batch id must be provided"));
            }
        }

        // Expire validation
        if (request.getExpireAt() == null) {
            errors.add(new FieldError("expireAt", "Expire date is required"));
        } else if (request.getExpireAt().isBefore(LocalDateTime.now())) {
            errors.add(new FieldError("expireAt", "Expire date must be in the future"));
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }

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
                .isPinned(entity.getIsPinned())
                .publishedDate(entity.getPublishedDate())
                .expireAt(entity.getExpireAt())
                .instituteId(entity.getInstitute().getId())
                .courseId(entity.getCourse() != null ? entity.getCourse().getId() : null)
                .batchId(entity.getBatch() != null ? entity.getBatch().getId() : null)
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }

    private AnnouncementStatus mapStatus(AnnouncementCreateStatus status) {
        return switch (status) {
            case PUBLISHED -> AnnouncementStatus.PUBLISHED;
            case DRAFT -> AnnouncementStatus.DRAFT;
        };
    }

}
