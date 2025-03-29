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

import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.model.user_models.User;
import edu.vinu.response.ApiResponse;
import edu.vinu.service.auth.impl.JwtService;
import edu.vinu.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@CrossOrigin
@RestController
@RequestMapping("api/v2/users")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getUserDetails(@RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        User user = userService.getUserByEmail(username);
        return ResponseEntity.ok(new ApiResponse("User Verified!",user));
    }

    @GetMapping("/user/by-email")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam String email){
        User userByEmail = userService.getUserByEmail(email);
        return ResponseEntity.status(FOUND).body(new ApiResponse("User Found By "+email,userByEmail));
    }

    @GetMapping("/by-firstname/{firstName}")
    public ResponseEntity<ApiResponse> getUsersByFirstName(@PathVariable String firstName){
        List<User> userList = userService.getAllUsersByFirstNameLike(firstName);
        return ResponseEntity.status(FOUND).body(new ApiResponse("User List Found by "+firstName,userList));
    }

    @GetMapping("/all-students")
    public ResponseEntity<ApiResponse> getAllStudents(){
        List<Student> studentList = userService.getAllStudents();
        return ResponseEntity.status(FOUND).body(new ApiResponse("All Students!",studentList));
    }

    @GetMapping("/all-teachers")
    public ResponseEntity<ApiResponse> getAllTeachers(){
        List<Teacher> teacherList = userService.getAllTeachers();
        return ResponseEntity.status(FOUND).body(new ApiResponse("All Teachers!",teacherList));
    }
}
