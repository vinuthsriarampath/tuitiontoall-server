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
import edu.vinu.events.CourseCreatedEvent;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.model.Course;
import edu.vinu.repository.CourseRepository;
import edu.vinu.repository.InstituteRepository;
import edu.vinu.request.CourseCreateRequest;
import edu.vinu.request.CourseFilterRequest;
import edu.vinu.request.CourseUpdateRequest;
import edu.vinu.service.common.CourseService;
import edu.vinu.service.common.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final ModelMapper mapper;
    private final CourseRepository courseRepository;
    private final InstituteRepository instituteRepository;
    private final Environment env;
    private final FileService fileService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${file.course.thumbnail-path}")
    private String courseThumbnailPath;

    @Transactional
    @Override
    public Course createCourse(CourseCreateRequest courseCreateRequest, MultipartFile thumbnail) {
        CourseEntity courseEntity = mapper.map(courseCreateRequest, CourseEntity.class);
        InstituteEntity institute = instituteRepository.findInstituteByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NotFoundException("Institute not found!"));

        courseEntity.setInstitute(institute);

        if (isValidThumbnail(thumbnail)) courseEntity.setThumbnail(saveThumbnail(thumbnail, courseEntity, institute));

        CourseEntity savedEntity = courseRepository.save(courseEntity);

        eventPublisher.publishEvent(new CourseCreatedEvent(savedEntity));
        return mapper.map(savedEntity, Course.class);
    }

    @Override
    public Course updateCourse(Long courseId, CourseUpdateRequest updatedCourseDetails, MultipartFile thumbnail) {
        return courseRepository.findById(courseId)
                .map(oldCourseEntity -> {
                    if (oldCourseEntity.getInstitute().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                        CourseEntity updatedCourseEntity = mapper.map(updatedCourseDetails, CourseEntity.class);

                        updatedCourseEntity.setId(oldCourseEntity.getId());
                        updatedCourseEntity.setInstitute(oldCourseEntity.getInstitute());

                        if (isValidThumbnail(thumbnail)) {
                            updatedCourseEntity.setThumbnail(saveThumbnail(thumbnail, updatedCourseEntity, oldCourseEntity.getInstitute()));
                        } else {
                            updatedCourseEntity.setThumbnail(oldCourseEntity.getThumbnail());
                        }

                        CourseEntity saved = courseRepository.save(updatedCourseEntity);
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
                    if (isCourseOwner(courseEntity)) {
                        return mapper.map(courseEntity, Course.class);
                    } else {
                        throw new UnauthorizedException("You are not authorized to view this course");
                    }
                })
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));
    }

    @Override
    public CourseEntity getCourseEntityById(Long courseId) {
        return courseRepository.findById(courseId)
                .map(courseEntity -> {
                    if (isCourseOwner(courseEntity)){
                        return courseEntity;
                    } else {
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
                .map(courseEntity -> mapper.map(courseEntity, Course.class))
                .toList();
    }

    @Override
    public File loadThumbnail(String filename) {
        return fileService.getFile(getCourseThumbnailPath(), filename);
    }

    @Override
    @Transactional()
    public List<Course> getAllCoursesByInstituteId(Long instituteId, CourseFilterRequest filters) {

        return courseRepository.findAllByInstituteIdWithFilters(instituteId, filters.category(), filters.level(), filters.language(), filters.mode(), filters.status())
                .stream()
                .map(courseEntity -> mapper.map(courseEntity, Course.class))
                .toList();
    }

    @Override
    public Boolean isCourseOwner(CourseEntity courseEntity){
        return courseEntity.getInstitute().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private String saveThumbnail(MultipartFile thumbnail, CourseEntity courseEntity, InstituteEntity instituteEntity) {
        String filename = this.generateUniqueThumbnailFilename(courseEntity, instituteEntity, thumbnail.getOriginalFilename());
        fileService.saveFile(thumbnail, this.getCourseThumbnailPath(), filename, StandardCopyOption.REPLACE_EXISTING);
        return "/thumbnail/" + filename;
    }

    private String generateUniqueThumbnailFilename(CourseEntity courseEntity, InstituteEntity instituteEntity, String originalFileName) {
        String courseName = courseEntity.getTitle().trim().toLowerCase().replaceAll("[^a-z0-9]+", "-");
        String instituteName = instituteEntity.getInstituteName().trim().toLowerCase().replaceAll("[^a-z0-9]+", "-");

        return String.format("%s@%s-@%s%s", courseName, instituteName, UUID.randomUUID(), fileService.extractExtension(originalFileName));
    }

    private Path getCourseThumbnailPath() {
        return Path.of(courseThumbnailPath);
    }

    private Boolean isValidThumbnail(MultipartFile thumbnail) {
        if (thumbnail == null || thumbnail.isEmpty()) {
            return false;
        }
        String contentType = thumbnail.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"));
    }
}
