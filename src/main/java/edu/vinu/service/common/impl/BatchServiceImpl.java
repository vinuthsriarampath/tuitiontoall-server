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
import edu.vinu.entity.CourseEntity;
import edu.vinu.enums.BatchEnrollmentStatus;
import edu.vinu.enums.BatchStatus;
import edu.vinu.events.CourseCreatedEvent;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.model.Batch;
import edu.vinu.repository.BatchRepository;
import edu.vinu.request.BatchCreateRequest;
import edu.vinu.request.BatchUpdateRequest;
import edu.vinu.response.FieldError;
import edu.vinu.service.common.BatchService;
import edu.vinu.service.common.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final CourseService courseService;
    private final ModelMapper mapper;

    @Override
    public Batch createBatch(BatchCreateRequest request) {
        CourseEntity courseEntity = courseService.getCourseEntityById(request.getCourseId());
        return createBatchInternally(courseEntity,request);
    }

    @Override
    public Batch createBatch(CourseEntity course, BatchCreateRequest request) {
        return createBatchInternally(course,request);
    }

    @Override
    public List<Batch> getAllBatchesByCourseId(Long courseId) {
        return batchRepository.getAllBatchesByCourseId(courseId)
                .stream()
                .map(batchEntity -> {
                    Batch batch = mapper.map(batchEntity, Batch.class);
                    batch.setCourseId(courseId);
                    return batch;
                })
                .toList();
    }

    @Override
    public Batch getBatchById(Long batchId) {
        BatchEntity batchEntity = batchRepository.findById(batchId)
                .orElseThrow(() -> new NotFoundException("Batch with the given id does not exist"));
        Batch batch =  mapper.map(batchEntity, Batch.class);
        batch.setCourseId(batchEntity.getCourse().getId());
        return batch;
    }

    @Override
    public Batch updateBatchById(Long batchId, BatchUpdateRequest request) {
        List<FieldError> errors = new ArrayList<>();

        Batch oldBatch = this.getBatchById(batchId);
        if(oldBatch.getStart_date()!=null && oldBatch.getStart_date().isBefore(LocalDate.now())){
            if(!oldBatch.getName().equals(request.getName()) || !oldBatch.getCourseId().equals(request.getCourseId()) || !oldBatch.getStart_date().isEqual(request.getStart_date()) || !oldBatch.getStart_time().equals(request.getStart_time()) ){
                throw new InvalidInputException("Batch name, course, start date & time cannot be updated for a batch that has already started");
            }
        }


        if ((oldBatch.getStart_date() != null && !oldBatch.getStart_date().isEqual(request.getStart_date())) && !isBatchStartDateValid(request.getStart_date())) {
            errors.add(new FieldError("start_date", "Start date must be present or future"));
        }

        if (!isMaxSeatLimitValid(request.getIs_seat_limited(), request.getMax_seat_limit())) {
            errors.add(new FieldError("max_seat_limit", "Invalid max seat limit. If seat is limited, max seat limit should be a positive integer and greater than 0. If seat is not limited, max seat limit should be 0."));
        }

        if (!isValidStatus(request.getBatch_status(), request.getEnrollment_status())) {
            errors.add(new FieldError("enrollment_status", "Must be CLOSED if batch is COMPLETED"));
        }

        if (!oldBatch.getName().equals(request.getName()) && batchRepository.existsByNameAndCourseId(request.getName(), request.getCourseId())) {
            errors.add(new FieldError("name", "Batch with the same name already exists for this courseEntity"));
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }

        BatchEntity newBatchEntity =  mapper.map(request, BatchEntity.class);
        newBatchEntity.setId(oldBatch.getId());
        if(!oldBatch.getCourseId().equals(request.getCourseId())){
            newBatchEntity.setCourse(courseService.getCourseEntityById(request.getCourseId()));
        }
        BatchEntity savedBatchEntity=batchRepository.save(newBatchEntity);
        Batch savedBatch =  mapper.map(savedBatchEntity, Batch.class);
        savedBatch.setCourseId(savedBatchEntity.getCourse().getId());
        return savedBatch;
    }

    @Override
    public BatchEntity getBatchEntityById(Long batchId) {
        return batchRepository.findById(batchId)
                .orElseThrow(() -> new NotFoundException("Batch with the given id does not exist"));
    }

    @Override
    public Boolean isBatchOwner(BatchEntity batchEntity) {
        return batchEntity.getCourse().getInstitute().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public Boolean isBatchBelongToCourse(BatchEntity batchEntity, Long courseId) {
        return batchEntity.getCourse().getId().equals(courseId);
    }


    private Batch createBatchInternally(CourseEntity courseEntity,BatchCreateRequest request){
        List<FieldError> errors = new ArrayList<>();

        if (!isBatchStartDateValid(request.getStart_date())) {
            errors.add(new FieldError("start_date", "Start date must be present or future"));
        }

        if (!isMaxSeatLimitValid(request.getIs_seat_limited(), request.getMax_seat_limit())) {
            errors.add(new FieldError("max_seat_limit", "Invalid max seat limit. If seat is limited, max seat limit should be a positive integer and greater than 0. If seat is not limited, max seat limit should be 0."));
        }

        if (!isValidStatus(request.getBatch_status(), request.getEnrollment_status())) {
            errors.add(new FieldError("enrollment_status", "Must be CLOSED if batch is COMPLETED"));
        }

        if (batchRepository.existsByNameAndCourseId(request.getName(),courseEntity.getId())){
            errors.add(new FieldError("name", "Batch with the same name already exists for this courseEntity"));
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }

        BatchEntity batchEntity=mapper.map(request, BatchEntity.class);
        batchEntity.setId(null); //prevent mapper to automatically inserts a new id for the batchEntity which is not required as it will be generated by the database
        batchEntity.setCourse(courseEntity);
        BatchEntity savedBatchEntity = batchRepository.save(batchEntity);
        Batch savedBatch = mapper.map(savedBatchEntity, Batch.class);
        savedBatch.setCourseId(courseEntity.getId());
        return savedBatch;
    }

    private Boolean isBatchStartDateValid(LocalDate date){
        return  !date.isBefore(LocalDate.now());
    }

    private Boolean isMaxSeatLimitValid(Boolean isSeatLimited, Integer maxSeatLimit){
        if(isSeatLimited){
            return maxSeatLimit != null && maxSeatLimit > 0;
        }
        return maxSeatLimit != null && maxSeatLimit == 0; // If seat is not limited, we consider it valid regardless of maxSeatLimit value
    }

    private boolean isValidStatus(BatchStatus batchStatus,BatchEnrollmentStatus batchEnrollmentStatus){
        return !batchStatus.equals(BatchStatus.COMPLETED) || batchEnrollmentStatus.equals(BatchEnrollmentStatus.CLOSED);
    }

    @Transactional
    @EventListener
    public void handleCourseCreated(CourseCreatedEvent event) {
        CourseEntity course = event.course();

        BatchCreateRequest request = new BatchCreateRequest(
                course.getId(),
                "DEFAULT-" + course.getId(),
                false,
                0,
                course.getCreationTimeStamp().toLocalDate().plusDays(1),
                course.getCreationTimeStamp().toLocalTime(),
                BatchStatus.PREPARATION,
                BatchEnrollmentStatus.OPEN
        );

        createBatchInternally(course, request);
    }
}
