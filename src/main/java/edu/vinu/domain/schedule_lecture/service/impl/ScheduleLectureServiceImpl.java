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

package edu.vinu.domain.schedule_lecture.service.impl;

import edu.vinu.common.dto.FieldError;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.chapter.entity.ChapterEntity;
import edu.vinu.domain.chapter.service.ChapterService;
import edu.vinu.domain.schedule_lecture.entity.ScheduleLectureEntity;
import edu.vinu.domain.schedule_lecture.enums.ScheduleLectureStatus;
import edu.vinu.domain.schedule_lecture.mapper.ScheduleLectureMapper;
import edu.vinu.domain.schedule_lecture.repository.ScheduleLectureRepository;
import edu.vinu.domain.schedule_lecture.request.create.ScheduleLectureCreateRequest;
import edu.vinu.domain.schedule_lecture.request.update.ScheduleLectureUpdateRequest;
import edu.vinu.domain.schedule_lecture.response.ScheduleLectureResponse;
import edu.vinu.domain.schedule_lecture.service.ScheduleLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        validateSchedule(entity.getChapter().getId(), request.startDate(),request.startTime(),request.endTime(), entity);

        entity.setStartDate(request.startDate());
        entity.setStartTime(request.startTime());
        entity.setEndTime(request.endTime());
        entity.setTopic(request.topic());
        entity.setLateAttendance(request.lateAttendance());
        entity.setMeetingUrl(request.meetingUrl());
        entity.setStatus(request.status());

        return ScheduleLectureMapper.toScheduleLectureResponse(scheduleLectureRepository.save(entity));
    }

    private ScheduleLectureEntity getScheduleLectureEntity(Long id){
        return scheduleLectureRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Schedule lecture with id " + id + " not found!"));
    }

    private void validateSchedule(Long chapterId,LocalDate startDate,LocalTime startTime, LocalTime endTime, @Nullable ScheduleLectureEntity existingEntity){

        boolean startDateChanged = true;
        boolean startTimeChanged = true;
        boolean endTimeChanged = true;

        if (existingEntity != null){
            startDateChanged = !existingEntity.getStartDate().isEqual(startDate);
            startTimeChanged = !existingEntity.getStartTime().equals(startTime);
            endTimeChanged = !existingEntity.getEndTime().equals(endTime);
        }
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(startDate, endTime);

        List<FieldError> errors = new ArrayList<>();

        if(startDateChanged && startDate.isBefore(LocalDate.now())){
                errors.add(new FieldError("startDate", "Meeting start date time must be in the future!"));
        }

        if (startTimeChanged && startDateTime.isBefore(LocalDateTime.now())){
                errors.add(new FieldError("startTime", "Meeting start date time must be in the future!"));
        }

        if(endTimeChanged && !endDateTime.isAfter(startDateTime)){
                errors.add(new FieldError("endTime", "Meeting end time must be after start time!"));
        }

        Long excludedId = existingEntity != null ? existingEntity.getId() : null;

        if ((startDateChanged || startTimeChanged || endTimeChanged) && isAlreadyExistsInSameTimePeriodInChapter(chapterId, startDate, startTime, endTime, excludedId)){
            errors.add(new FieldError("startDate", "You cannot schedule lectures with overlapping time periods and lectures with draft,scheduled,live and completed status on the same day!"));
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }
    }

    private boolean isAlreadyExistsInSameTimePeriodInChapter(Long chapterId,LocalDate startDate,LocalTime startTime, LocalTime endTime,Long excludedId){
        return scheduleLectureRepository.existsInSameTimePeriodInChapter(chapterId, startDate, startTime, endTime, excludedId) == 1;
    }
}
