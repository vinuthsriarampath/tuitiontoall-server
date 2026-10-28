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

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.course.dto.Course;
import edu.vinu.domain.course.request.CourseCreateRequest;
import edu.vinu.domain.course.request.CourseFilterRequest;
import edu.vinu.domain.course.request.CourseUpdateRequest;
import edu.vinu.domain.course.response.CourseStatsResponse;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.course.service.CourseStatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@RestController
@RequestMapping("api/v2/courses")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class CourseController {

    private final CourseService courseService;
    private final CourseStatsService courseStatsService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestPart("course") @Valid CourseCreateRequest course, @RequestPart(value = "thumbnail",required = false)MultipartFile thumbnail){
        return ResponseEntity.status(201).body(createApiResponse("Course created successfully", courseService.createCourse(course,thumbnail)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping(value = "/update/{courseId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<Course>> updateCourse(@PathVariable Long courseId,@Valid @RequestPart("course") CourseUpdateRequest updatedCourseDetails, @RequestPart(value = "thumbnail",required = false)MultipartFile thumbnail){
        return ResponseEntity.status(200).body(createApiResponse("Course updated successfully", courseService.updateCourse(courseId, updatedCourseDetails, thumbnail) ));
    }

    @PreAuthorize(("hasAuthority('institute')"))
    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long courseId){
        courseService.deleteCourse(courseId);
        return ResponseEntity.status(200).body(createApiResponse("Course deleted successfully", null ));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/archive/{courseId}")
    public ResponseEntity<ApiResponse<Course>> archiveCourse(@PathVariable Long courseId){
        return ResponseEntity.status(200).body(createApiResponse("Course archived successfully", courseService.archiveCourse(courseId)));
    }

    @PreAuthorize("hasAnyAuthority('institute','student')")
    @GetMapping("/get/{courseId}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable Long courseId){
        return ResponseEntity.status(200).body(createApiResponse("Course Found!",courseService.getCourseById(courseId)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/institute")
    public ResponseEntity<PaginatedApiResponse<Course>> getPaginatedCoursesForCurrentInstitute(PaginationRequest pagination, CourseFilterRequest filters){
        return ResponseEntity.ok(courseService.getPaginatedCoursesForCurrentInstitute(pagination ,filters));
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

    @GetMapping("institute/{instituteId}")
    public ResponseEntity<ApiResponse<List<Course>>> getAllCoursesByInstituteId(@PathVariable Long instituteId,CourseFilterRequest filters){
        return ResponseEntity.status(200).body(createApiResponse("Courses for the institute", courseService.getAllCoursesByInstituteId(instituteId,filters)));
    }

    @GetMapping("{courseId}/stats")
    public ResponseEntity<ApiResponse<CourseStatsResponse>> getCourseStats(@PathVariable Long courseId){
        return ResponseEntity.ok(courseStatsService.getCourseStats(courseId));
    }

}
