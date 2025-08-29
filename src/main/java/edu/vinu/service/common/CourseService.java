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

import edu.vinu.exception.custom.NotFoundException;
import edu.vinu.exception.custom.UnauthorizedException;
import edu.vinu.model.Course;
import edu.vinu.request.CourseCreateRequest;
import edu.vinu.request.CourseUpdateRequest;

public interface CourseService {

    /**
     * Creates a new course and associates it with the currently authenticated institute.
     * @param course the details of the course to be created
     * @return the created Course object
     * @throws NotFoundException if the currently authenticated institute is not found
     */
    Course createCourse(CourseCreateRequest course);

    /**
     * Updates an existing course with new details.
     * @param courseId the ID of the course to be updated
     * @param updatedCourseDetails the new details for the course
     * @return the updated Course object
     * @throws NotFoundException if the course with the given ID does not exist
     * @throws UnauthorizedException if the current user is not authorized to update the course
     */
    Course updateCourse(Long courseId, CourseUpdateRequest updatedCourseDetails);

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
}
