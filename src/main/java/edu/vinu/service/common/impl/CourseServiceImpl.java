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
import edu.vinu.exception.custom.UserNotFoundException;
import edu.vinu.model.Course;
import edu.vinu.repository.CourseRepository;
import edu.vinu.repository.UserRepository;
import edu.vinu.request.CourseCreateRequest;
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
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        courseEntity.setInstitute(institute);
        CourseEntity savedEntity = courseRepository.save(courseEntity);
        return mapper.map(savedEntity,Course.class);
    }
}
