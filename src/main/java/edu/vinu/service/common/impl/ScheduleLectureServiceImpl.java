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

import edu.vinu.entity.ChapterEntity;
import edu.vinu.entity.ScheduleLectureEntity;
import edu.vinu.enums.ScheduleLectureStatus;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.mapper.ScheduleLectureMapper;
import edu.vinu.repository.ScheduleLectureRepository;
import edu.vinu.request.schedule_lecture.ScheduleLectureCreateRequest;
import edu.vinu.request.schedule_lecture.ScheduleLectureUpdateRequest;
import edu.vinu.response.FieldError;
import edu.vinu.response.schedule_lecture.ScheduleLectureResponse;
import edu.vinu.service.common.ChapterService;
import edu.vinu.service.common.ScheduleLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleLectureServiceImpl implements ScheduleLectureService {
    private final ScheduleLectureRepository scheduleLectureRepository;
    private final ChapterService chapterService;

    @Override
    public ScheduleLectureResponse scheduleLecture(ScheduleLectureCreateRequest request) {
        validateSchedule(request.chapterId(), request.startDate(),request.startTime(),request.endTime(),null);

        ChapterEntity chapterEntity = chapterService.getChapterEntityById(request.chapterId());

        ScheduleLectureEntity scheduleLectureEntity =  ScheduleLectureEntity.builder()
                .chapter(chapterEntity)
                .topic(request.topic())
                .startDate(request.startDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .lateAttendance(request.lateAttendance())
                .meetingUrl(request.meetingUrl())
                .status(ScheduleLectureStatus.valueOf(request.status().name()))
                .build();

        return ScheduleLectureMapper.toScheduleLectureResponse(scheduleLectureRepository.save(scheduleLectureEntity));
    }

    @Override
    public ScheduleLectureResponse updateScheduleLecture(Long id, ScheduleLectureUpdateRequest request) {
        ScheduleLectureEntity entity = getScheduleLectureEntity(id);

        if(!entity.getStartDate().isEqual(request.startDate()) || !entity.getStartTime().equals(request.startTime()) || !entity.getEndTime().equals(request.endTime())){
            validateSchedule(entity.getChapter().getId(), request.startDate(),request.startTime(),request.endTime(), entity.getId());
            entity.setStartDate(request.startDate());
            entity.setStartTime(request.startTime());
            entity.setEndTime(request.endTime());
        }

        entity.setTopic(request.topic());
        entity.setLateAttendance(request.lateAttendance());
        entity.setMeetingUrl(request.meetingUrl());
        entity.setStatus(request.status());

        return ScheduleLectureMapper.toScheduleLectureResponse(scheduleLectureRepository.save(entity));
    }

    private ScheduleLectureEntity getScheduleLectureEntity(Long id){
        return scheduleLectureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule lecture with id " + id + " not found!"));
    }

    private void validateSchedule(Long chapterId,LocalDate startDate,LocalTime startTime, LocalTime endTime, Long excludedId){
        List<FieldError>  errors = new ArrayList<>();

        if(!isValidStartTime(startTime,endTime)){
            errors.add(new FieldError("startTime","Start time must be a time before end time!"));
        }
        if (!isValidEndTime(startTime,endTime)) {
            errors.add(new FieldError("endTime","End time must be a time after start time!"));
        }

        if(isAlreadyExistsInSameTimePeriodInChapter(chapterId, startDate, startTime, endTime, excludedId)){
            errors.add(new FieldError("startDate","You cannot schedule lectures with overlapping time periods on the same day!"));
        }

        if(!errors.isEmpty()){
            throw new InvalidInputException(errors);
        }
    }

    private boolean isValidStartTime(LocalTime startTime, LocalTime endTime){
        return startTime.isAfter(LocalTime.now()) && startTime.isBefore(endTime);
    }

    private boolean isValidEndTime(LocalTime startTime, LocalTime endTime){
        return endTime.isAfter(LocalTime.now()) && endTime.isAfter(startTime);
    }

    private boolean isAlreadyExistsInSameTimePeriodInChapter(Long chapterId,LocalDate startDate,LocalTime startTime, LocalTime endTime,Long excludedId){
        return scheduleLectureRepository.existsInSameTimePeriodInChapter(chapterId, startDate, startTime, endTime, excludedId) == 1;
    }
}
