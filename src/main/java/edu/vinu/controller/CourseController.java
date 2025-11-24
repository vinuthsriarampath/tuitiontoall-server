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

package edu.vinu.controller;

import edu.vinu.model.Course;
import edu.vinu.request.CourseCreateRequest;
import edu.vinu.request.CourseUpdateRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.service.common.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static edu.vinu.validator.UserValidator.USER_VALIDATION_FAILED_ERROR;

@RestController
@RequestMapping("api/v2/courses")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCourse(@Valid @RequestBody CourseCreateRequest course, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponse(USER_VALIDATION_FAILED_ERROR, errors));
        }
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.status(201).body(new ApiResponse("Course created successfully", createdCourse ));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/update/{courseId}")
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Long courseId,@Valid @RequestBody CourseUpdateRequest updatedCourseDetails, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponse(USER_VALIDATION_FAILED_ERROR, errors));
        }
        Course updatedCourse = courseService.updateCourse(courseId, updatedCourseDetails);
        return ResponseEntity.status(200).body(new ApiResponse("Course updated successfully", updatedCourse ));
    }

    @PreAuthorize(("hasAuthority('institute')"))
    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Long courseId){
        courseService.deleteCourse(courseId);
        return ResponseEntity.status(200).body(new ApiResponse("Course deleted successfully", null ));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/archive/{courseId}")
    public ResponseEntity<ApiResponse> archiveCourse(@PathVariable Long courseId){
        Course archivedCourse=courseService.archiveCourse(courseId);
        return ResponseEntity.status(200).body(new ApiResponse("Course archived successfully", archivedCourse ));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/get/{courseId}")
    public ResponseEntity<ApiResponse> getCourseById(@PathVariable Long courseId){
        Course course = courseService.getCourseById(courseId);
        return ResponseEntity.status(200).body(new ApiResponse("Course Found!",course));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/institute/all")
    public ResponseEntity<ApiResponse> getAllCoursesForInstitute(){
        return ResponseEntity.status(200).body(new ApiResponse("All courses for the institute", courseService.getAllCoursesForInstitute()));
    }
}
