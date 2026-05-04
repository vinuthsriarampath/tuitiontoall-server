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

package edu.vinu.controller;

import edu.vinu.entity.AnnouncementEntity;
import edu.vinu.request.announcements.AnnouncementCreateRequest;
import edu.vinu.request.announcements.AnnouncementFilterRequest;
import edu.vinu.request.announcements.AnnouncementUpdateRequest;
import edu.vinu.request.announcements.AnnouncementVisibilityUpdateRequest;
import edu.vinu.response.AnnouncementResponse;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.PaginatedApiResponse;
import edu.vinu.service.common.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping
    public ResponseEntity<ApiResponse> createAnnouncement(@Valid @RequestBody AnnouncementCreateRequest request){
        AnnouncementResponse response = announcementService.createAnnouncement(request);
        return ResponseEntity.status(201).body(new ApiResponse("Announcement created successfully!", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{announcementId}/visibility")
    public ResponseEntity<ApiResponse> updateAnnouncementVisibility(@PathVariable Long announcementId, @Valid @RequestBody AnnouncementVisibilityUpdateRequest request){
        AnnouncementResponse response = announcementService.updateAnnouncementVisibility(announcementId, request);
        return ResponseEntity.status(200).body(new ApiResponse("Announcement visibility updated successfully!", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{announcementId}")
    public ResponseEntity<ApiResponse> updateAnnouncementTitleAndDescription(@PathVariable Long announcementId, @RequestBody AnnouncementUpdateRequest request){
        AnnouncementResponse response = announcementService.updateAnnouncementTitleAndDescription(announcementId, request);
        return ResponseEntity.status(200).body(new ApiResponse("Announcement updated successfully!", response));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<ApiResponse> archiveAnnouncement(@PathVariable Long id){
        AnnouncementResponse response = announcementService.archiveAnnouncementById(id);
        return ResponseEntity.status(200).body(new ApiResponse("Announcement Archived successfully!", response));
    }

    @PatchMapping("/{id}/pin")
    public ResponseEntity<ApiResponse> pinAnnouncement(@PathVariable Long id){
        AnnouncementResponse response =  announcementService.pinAnnouncementById(id);
        return ResponseEntity.status(200).body(new ApiResponse("Announcement Pin successfully!", response));
    }

    @PatchMapping("/{id}/unpin")
    public ResponseEntity<ApiResponse> unpinAnnouncement(@PathVariable Long id){
        AnnouncementResponse response =  announcementService.unpinAnnouncementById(id);
        return ResponseEntity.status(200).body(new ApiResponse("Announcement Unpin successfully!", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping
    public ResponseEntity<PaginatedApiResponse<AnnouncementResponse>> getAllAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "published_date") List<String> sortBy,
            AnnouncementFilterRequest filters
            ){
        Page<AnnouncementResponse> pageData = announcementService.getAllAnnouncements(page,size,direction,sortBy,filters);
        PaginatedApiResponse<AnnouncementResponse> response = PaginatedApiResponse.<AnnouncementResponse>builder()
                .message("Announcements fetched successfully!")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .last(pageData.isLast())
                .build();
        return ResponseEntity.status(200).body(response);
    }
}
