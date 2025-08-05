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

import edu.vinu.service.common.ProfileFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@CrossOrigin
@RestController
@RequestMapping("api/v2/profile-files")
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ProfileFileController {

    private final ProfileFileService profileFileService;

    @PostMapping("/upload/{type:dp|banner}")
    public ResponseEntity<String> uploadFile(@PathVariable String type, @RequestParam("file") MultipartFile file){
        String filename = profileFileService.uploadFile(file, type);
        return ResponseEntity.ok("http://localhost:8080/uploads/profiles/dp/" + filename);
    }

    @GetMapping("/load/{type:dp|banner}/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable("type") String type,@PathVariable("filename") String fileName) throws IOException {
        System.out.println("test image Load controller");
        Resource resource = profileFileService.loadFile(type,fileName);
        System.out.println(resource);
        String contentType = Files.probeContentType(resource.getFile().toPath());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
