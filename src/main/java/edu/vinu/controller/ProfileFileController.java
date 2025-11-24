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
import edu.vinu.service.common.ProfileFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@CrossOrigin
@RestController
@RequestMapping("api/v2/profile-files")
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ProfileFileController {

    private final ProfileFileService profileFileService;

    @PostMapping("/upload/{type:dp|banner}")
    public ResponseEntity<ApiResponse> uploadFile(@PathVariable String type, @RequestParam("file") MultipartFile file){
        String path = profileFileService.uploadFile(file, type);
        return ResponseEntity.status(200).body(new ApiResponse("File uploaded successfully", path));
    }

    @GetMapping("/load/{type:dp|banner}/{filename:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable("type") String type,@PathVariable("filename") String fileName) throws IOException {
        File file = profileFileService.loadFile(type, fileName);

        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(file.toPath());
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(fileBytes);
    }
}
