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

package edu.vinu.domain.user.controller;

import edu.vinu.model.user_models.Institute;
import edu.vinu.domain.user.dto.Student;
import edu.vinu.domain.user.dto.Teacher;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.institute.request.InstituteDetailsUpdateRequest;
import edu.vinu.domain.user.request.update.StudentDetailsUpdateRequest;
import edu.vinu.domain.user.request.update.TeacherDetailsUpdateRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("api/v2/users")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getUserDetails(){
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(new ApiResponse("User Verified!",user));
    }

    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam String email){
        User userByEmail = userService.getUserByEmail(email);
        return ResponseEntity.status(FOUND).body(new ApiResponse("User Found By "+email,userByEmail));
    }

    @GetMapping("/by-firstname/{firstName}")
    public ResponseEntity<ApiResponse> getUsersByFirstName(@PathVariable String firstName){
        List<Object> userList = userService.getAllUsersByFirstNameLike(firstName);
        return ResponseEntity.status(FOUND).body(new ApiResponse("User List Found by "+firstName,userList));
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse> getAllStudents(){
        List<Student> studentList = userService.getAllStudents();
        return ResponseEntity.status(FOUND).body(new ApiResponse("All Students!",studentList));
    }

    @GetMapping("/teachers")
    public ResponseEntity<ApiResponse> getAllTeachers(){
        List<Teacher> teacherList = userService.getAllTeachers();
        return ResponseEntity.status(FOUND).body(new ApiResponse("All Teachers!",teacherList));
    }

    @GetMapping("/institutes")
    public ResponseEntity<ApiResponse> getAllInstitutes(){
        List<Institute> instituteList=userService.getAllInstitutes();
        return ResponseEntity.status(FOUND).body(new ApiResponse("All Institutes!",instituteList));
    }

    @GetMapping("/institutes/by-name/{instituteName}")
    public ResponseEntity<ApiResponse> getInstitutesByName(@PathVariable String instituteName){
        List<User> instituteList = userService.getAllInstitutesByInstituteName(instituteName);
        return ResponseEntity.status(FOUND).body(new ApiResponse("Related institutes for "+instituteName,instituteList));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/institutes/update/me")
    public ResponseEntity<ApiResponse> updateInstituteDetails(@Valid @RequestBody InstituteDetailsUpdateRequest instituteDetailsUpdateRequest){
        Institute updateInstituteDetails = userService.updateInstituteDetails(SecurityContextHolder.getContext().getAuthentication().getName(),instituteDetailsUpdateRequest);
        return ResponseEntity.status(OK).body(new ApiResponse("Profile Updated",updateInstituteDetails));
    }

    @PreAuthorize("hasAuthority('teacher')")
    @PatchMapping("/teachers/update/me")
    public ResponseEntity<ApiResponse> updateTeacherDetails(@Valid @RequestBody TeacherDetailsUpdateRequest teacherDetailsUpdateRequest){
        Teacher updatedTeacherDetails = userService.updateTeacherDetails(SecurityContextHolder.getContext().getAuthentication().getName(),teacherDetailsUpdateRequest);
        return ResponseEntity.status(OK).body(new ApiResponse("Teacher Profile Updated!",updatedTeacherDetails));
    }

    @PreAuthorize("hasAuthority('student')")
    @PatchMapping("/student/update/me")
    public ResponseEntity<ApiResponse> updateStudentDetails(@Valid @RequestBody StudentDetailsUpdateRequest studentDetailsUpdateRequest){
        Student updatedStudentDetails = userService.updateStudentDetails(SecurityContextHolder.getContext().getAuthentication().getName(),studentDetailsUpdateRequest);
        return ResponseEntity.status(OK).body(new ApiResponse("Student Profile Updated!",updatedStudentDetails));
    }

    @DeleteMapping("/disable/me")
    public ResponseEntity<ApiResponse> disableMyAccount(){
        userService.disableUserAccountByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.status(OK).body(new ApiResponse("User disabled successfully!",null));
    }

    @GetMapping("/by-user-slug/{userSlug}")
    public ResponseEntity<ApiResponse> getUserByUserSlug(@PathVariable String userSlug){
        User user = userService.getUserByUserSlug(userSlug);
        return ResponseEntity.status(OK).body(new ApiResponse("User Found By "+userSlug,user));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("validate/institute-role")
    public ResponseEntity<ApiResponse> validateInstituteRole(){
        return ResponseEntity.status(OK).body(new ApiResponse("User has institute role!",null));
    }

}
