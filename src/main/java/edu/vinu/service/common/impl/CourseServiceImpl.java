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
import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.exception.custom.UnauthorizedException;
import edu.vinu.model.Course;
import edu.vinu.repository.CourseRepository;
import edu.vinu.repository.InstituteRepository;
import edu.vinu.request.CourseCreateRequest;
import edu.vinu.request.CourseUpdateRequest;
import edu.vinu.service.common.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final ModelMapper mapper;
    private final CourseRepository courseRepository;
    private final InstituteRepository instituteRepository;
    private final Environment env;

    @Override
    public Course createCourse(CourseCreateRequest courseCreateRequest, MultipartFile thumbnail) {
        CourseEntity courseEntity= mapper.map(courseCreateRequest,CourseEntity.class);
        InstituteEntity institute = instituteRepository.findInstituteByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NotFoundException("Institute not found!"));

        courseEntity.setInstitute(institute);

        try {
            if(thumbnail != null && !thumbnail.isEmpty()){
                String path = env.getProperty("file.course.thumbnail-path");
                if (path == null) {
                    throw new RuntimeException("Thumbnail path not configured");
                }
                Path dir = Path.of(path);
                Files.createDirectories(dir);

                String originalFilename = thumbnail.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                }

                String courseName = courseEntity.getTitle().trim().toLowerCase().replaceAll("[^a-z0-9]+","-");
                String instituteName = institute.getInstituteName().trim().toLowerCase().replaceAll("[^a-z0-9]+","-");

                String filename = String.format("%s@%s-@%s%s",courseName,instituteName, UUID.randomUUID().toString(),extension);

                Path filePath = dir.resolve(filename);
                Files.copy(thumbnail.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                courseEntity.setThumbnail("/thumbnail/"+filename);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CourseEntity savedEntity = courseRepository.save(courseEntity);
        return mapper.map(savedEntity,Course.class);
    }

    @Override
    public Course updateCourse(Long courseId, CourseUpdateRequest updatedCourseDetails) {
        return courseRepository.findById(courseId)
                .map(courseEntity -> {
                    if (courseEntity.getInstitute().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
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
                    if (courseEntity.getInstitute().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
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
                    if (courseEntity.getInstitute().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                        courseEntity.setStatus(CourseStatus.ARCHIVED);
                        CourseEntity saved = courseRepository.save(courseEntity);
                        return mapper.map(saved, Course.class);
                    } else {
                        throw new UnauthorizedException("You are not authorized to archive this course");
                    }
                })
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .map(courseEntity -> {
                    if (courseEntity.getInstitute().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
                        return mapper.map(courseEntity, Course.class);
                    }else {
                        throw new UnauthorizedException("You are not authorized to view this course");
                    }
                })
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
    }

    @Override
    public List<Course> getAllCoursesForInstitute() {
        InstituteEntity institute = instituteRepository.findInstituteByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NotFoundException("Institute not found!"));

        return courseRepository.findAllByInstituteId(institute.getId())
                .stream()
                .map( courseEntity -> mapper.map(courseEntity,Course.class))
                .toList();
    }

    @Override
    public File loadThumbnail(String filename) {
        String path = env.getProperty("file.course.thumbnail-path");
        if (path == null) {
            throw new RuntimeException("Thumbnail path not configured");
        }
        Path filePath = Paths.get(path).resolve(filename);
        return filePath.toFile();
    }
}
