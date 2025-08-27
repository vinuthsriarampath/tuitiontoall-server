/*
 * Copyright (c) 2025 vinuth sri arampath
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

import edu.vinu.entity.CourseEntity;
import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.enums.CourseStatus;
import edu.vinu.exception.custom.UnauthorizedException;
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.model.Course;
import edu.vinu.repository.CourseRepository;
import edu.vinu.repository.UserRepository;
import edu.vinu.request.CourseCreateRequest;
import edu.vinu.request.CourseUpdateRequest;
import edu.vinu.service.common.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final ModelMapper mapper;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public Course createCourse(CourseCreateRequest course) {
        CourseEntity courseEntity= mapper.map(course,CourseEntity.class);
        InstituteEntity institute = Optional.ofNullable(userRepository.findInstituteByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new NotFoundException("User not found!"));

        courseEntity.setInstitute(institute);
        CourseEntity savedEntity = courseRepository.save(courseEntity);
        return mapper.map(savedEntity,Course.class);
    }

    @Override
    public Course updateCourse(Long courseId, CourseUpdateRequest updatedCourseDetails) {
        return courseRepository.findById(courseId)
                .map(courseEntity -> {
                    if (courseEntity.getInstitute().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
                        CourseEntity newCourseDetails = mapper.map(updatedCourseDetails, CourseEntity.class);
                        newCourseDetails.setId(courseEntity.getId());
                        newCourseDetails.setInstitute(courseEntity.getInstitute());
                        CourseEntity saved = courseRepository.save(newCourseDetails);
                        return mapper.map(saved, Course.class);
                    } else {
                        throw new UnauthorizedException("You are not authorized to update this course");
                    }
                })
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
    }

    @Override
    public void deleteCourse(Long courseId) {
        courseRepository.findById(courseId)
                .ifPresentOrElse(courseEntity -> {
                    if (courseEntity.getInstitute().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                        courseRepository.delete(courseEntity);
                    } else {
                        throw new UnauthorizedException("You are not authorized to delete this course");
                    }
                }, () -> {
                    throw new NotFoundException("Course not found with id: " + courseId);
                });
    }


    @Override
    public Course archiveCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .map(courseEntity -> {
                    if (courseEntity.getInstitute().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                        courseEntity.setStatus(CourseStatus.ARCHIVED);
                        CourseEntity saved = courseRepository.save(courseEntity);
                        return mapper.map(saved, Course.class);
                    } else {
                        throw new UnauthorizedException("You are not authorized to archive this course");
                    }
                })
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
    }
}
