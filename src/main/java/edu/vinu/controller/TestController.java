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

import edu.vinu.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v2/greetings")
public class TestController {
    List<String> greetings = new ArrayList<>();

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllGreetings() {
        return ResponseEntity.ok(new ApiResponse("Greetings", greetings));
    }

    @PreAuthorize("hasRole('INSTITUTE')")
    @PostMapping("/greet/add/{greet}")
    public ResponseEntity<ApiResponse> addGreet(@PathVariable String greet){
        greetings.add(greet);
        return ResponseEntity.ok(new ApiResponse("Greet added", null));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/greet/delete/{greet}")
    public ResponseEntity<ApiResponse> deleteGreet(@PathVariable String greet) {
        greetings.remove(greet);
        return ResponseEntity.ok(new ApiResponse("Greet deleted", null));
    }
}
