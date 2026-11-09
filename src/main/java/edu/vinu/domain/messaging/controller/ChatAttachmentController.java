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

package edu.vinu.domain.messaging.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.messaging.dtos.response.TempMessageAttachmentResponse;
import edu.vinu.domain.messaging.services.ChatAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@RestController
@RequestMapping("/api/v2/chat/attachments")
@RequiredArgsConstructor
public class ChatAttachmentController {

    private final ChatAttachmentService attachmentService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<List<TempMessageAttachmentResponse>>> uploadChatFiles(@RequestParam("files") List<MultipartFile> files) {

        List<TempMessageAttachmentResponse> uploadedFiles = attachmentService.tempStoreFiles(files);
        return ResponseEntity.ok(createApiResponse("Files uploaded successfully for messaging", uploadedFiles));
    }

    @GetMapping("/load/{filename:.+}")
    public ResponseEntity<Resource> getAttachment(@PathVariable String filename){
        return attachmentService.loadAttachments(filename);
    }

    @GetMapping("/stream/{filename:.+}")
    public ResponseEntity<ResourceRegion> streamAttachment(@PathVariable String filename, @RequestHeader(value = "Range", required = false) String rangeHeader) {
        return attachmentService.streamAttachment(filename, rangeHeader);
    }
}
