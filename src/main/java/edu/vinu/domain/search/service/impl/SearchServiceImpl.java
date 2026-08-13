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

package edu.vinu.domain.search.service.impl;

import edu.vinu.domain.institute.service.InstituteService;
import edu.vinu.domain.search.response.SearchResponse;
import edu.vinu.domain.search.service.SearchService;
import edu.vinu.domain.student.service.StudentService;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final InstituteService instituteService;

    @Async
    private CompletableFuture<List<User>> searchStudents(String query){
        return CompletableFuture.completedFuture(studentService.getAllStudentsByFirstName(query));
    }
    
    @Async
    private CompletableFuture<List<User>> searchTeachers(String query){
        return CompletableFuture.completedFuture(teacherService.getAllTeachersByFirstName(query));
    }
    
    @Async
    private CompletableFuture<List<User>> searchInstitutes(String query){
        return CompletableFuture.completedFuture(instituteService.getAllInstitutesByName(query));
    }

    @Override
    public SearchResponse search(String query) {
        try {
            CompletableFuture<List<User>> students = searchStudents(query);
            CompletableFuture<List<User>> teachers = searchTeachers(query);
            CompletableFuture<List<User>> institutes = searchInstitutes(query);

            CompletableFuture.allOf(students, teachers, institutes).join();
            return new SearchResponse(
                    students.get(),
                    teachers.get(),
                    institutes.get()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
