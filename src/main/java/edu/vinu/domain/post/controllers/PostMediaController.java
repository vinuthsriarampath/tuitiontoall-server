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

package edu.vinu.domain.post.controllers;

import edu.vinu.domain.post.services.PostMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/posts/media")
@RequiredArgsConstructor
public class PostMediaController {
    private final PostMediaService postMediaService;

    @GetMapping("/{fileName}")
    public ResponseEntity<?> getMediaFile(@PathVariable String fileName, @RequestHeader(value = "Range", required = false) String rangeHeader) {

        return postMediaService.loadMediaResource(fileName, rangeHeader);
    }
}
