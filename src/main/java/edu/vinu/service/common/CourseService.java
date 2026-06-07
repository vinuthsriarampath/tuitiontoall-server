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

package edu.vinu.service.common;

import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.domain.course.dto.Course;
import edu.vinu.request.CourseCreateRequest;
import edu.vinu.request.CourseFilterRequest;
import edu.vinu.request.CourseUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface CourseService {

    /**
     * Creates a new course and associates it with the currently authenticated institute.
     * @param courseCreateRequest the details of the course to be created
     * @param thumbnail the thumbnail image file for the course
     * @return the created Course object
     * @throws NotFoundException if the currently authenticated institute is not found
     */
    Course createCourse(CourseCreateRequest courseCreateRequest, MultipartFile thumbnail);

    /**
     * Updates an existing course with new details.
     *
     * @param courseId             the ID of the course to be updated
     * @param updatedCourseDetails the new details for the course
     * @param thumbnail
     * @return the updated Course object
     * @throws NotFoundException     if the course with the given ID does not exist
     * @throws UnauthorizedException if the current user is not authorized to update the course
     */
    Course updateCourse(Long courseId, CourseUpdateRequest updatedCourseDetails, MultipartFile thumbnail);

    /**
     * Deletes a course by its ID.
     * @param courseId the ID of the course to be deleted
     */
    void deleteCourse(Long courseId);

    /**
     * Archive a course by its ID.
     * @param courseId the ID of the course to be archived
     */
    Course archiveCourse(Long courseId);

    /**
     * Retrieves a course by its ID.
     * @param courseId the ID of the course to be retrieved
     * @return the Course object
     * @throws NotFoundException if the course with the given ID does not exist
     */
    Course getCourseById(Long courseId);


    CourseEntity getCourseEntityById(Long courseId);
    /**
     * Retrieves all courses associated with the currently authenticated institute.
     * @return a list of Course objects
     */
    List<Course> getAllCoursesForInstitute();

    /**
     * Retrieves the thumbnail based on filename
     * @param filename name of the filename
     * @return File
     */
    File loadThumbnail(String filename);

    /**
     * Get all courses by institute id
     * @param instituteId id of the institute
     * @param filters filters for the courses
     * @return list of courses
     */
    List<Course> getAllCoursesByInstituteId(Long instituteId, CourseFilterRequest filters);

    Boolean isCourseOwner(CourseEntity courseEntity);
}
