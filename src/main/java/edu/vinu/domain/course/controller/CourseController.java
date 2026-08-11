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

package edu.vinu.domain.course.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.course.dto.Course;
import edu.vinu.domain.course.request.CourseCreateRequest;
import edu.vinu.domain.course.request.CourseFilterRequest;
import edu.vinu.domain.course.request.CourseUpdateRequest;
import edu.vinu.domain.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.vinu.domain.user.validator.UserValidator.USER_VALIDATION_FAILED_ERROR;

@RestController
@RequestMapping("api/v2/courses")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> createCourse(@RequestPart("course") @Valid CourseCreateRequest course, BindingResult bindingResult, @RequestPart(value = "thumbnail",required = false)MultipartFile thumbnail){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponse(USER_VALIDATION_FAILED_ERROR, errors));
        }
        Course createdCourse = courseService.createCourse(course,thumbnail);
        return ResponseEntity.status(201).body(new ApiResponse("Course created successfully", createdCourse ));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping(value = "/update/{courseId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Long courseId,@Valid @RequestPart("course") CourseUpdateRequest updatedCourseDetails, @RequestPart(value = "thumbnail",required = false)MultipartFile thumbnail, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponse(USER_VALIDATION_FAILED_ERROR, errors));
        }
        Course updatedCourse = courseService.updateCourse(courseId, updatedCourseDetails, thumbnail);
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

    @PreAuthorize("hasAnyAuthority('institute','student')")
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

    @GetMapping("/thumbnail/{filename:.+}")
    public ResponseEntity<byte[]> loadThumbnail(@PathVariable("filename") String filename) throws IOException {
        File thumbnail = courseService.loadThumbnail(filename);

        if(!thumbnail.exists() || !thumbnail.isFile()){
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(thumbnail.toPath());
        byte[] fileBytes = Files.readAllBytes(thumbnail.toPath());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(fileBytes);
    }

    @GetMapping("/{instituteId}/all")
    public ResponseEntity<ApiResponse> getAllCoursesByInstituteId(@PathVariable Long instituteId,CourseFilterRequest filters
    ){
        List<Course> courses = courseService.getAllCoursesByInstituteId(instituteId,filters);
        return ResponseEntity.status(200).body(new ApiResponse("Courses for the institute", courses));
    }

}
