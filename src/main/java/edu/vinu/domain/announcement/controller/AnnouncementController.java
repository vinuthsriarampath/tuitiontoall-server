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

package edu.vinu.domain.announcement.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.announcement.request.AnnouncementFilterRequest;
import edu.vinu.domain.announcement.request.create.AnnouncementCreateRequest;
import edu.vinu.domain.announcement.request.update.AnnouncementUpdateRequest;
import edu.vinu.domain.announcement.request.update.AnnouncementVisibilityUpdateRequest;
import edu.vinu.domain.announcement.response.AnnouncementResponse;
import edu.vinu.domain.announcement.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;
import static edu.vinu.common.util.ResponseUtil.createPaginatedApiResponse;

@RestController
@RequestMapping("/api/v2/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping
    public ResponseEntity<ApiResponse<AnnouncementResponse>> createAnnouncement(@Valid @RequestBody AnnouncementCreateRequest request){
        AnnouncementResponse response = announcementService.createAnnouncement(request);
        return ResponseEntity.status(201).body(createApiResponse("Announcement created successfully!", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{announcementId}/visibility")
    public ResponseEntity<ApiResponse<AnnouncementResponse>> updateAnnouncementVisibility(@PathVariable Long announcementId, @Valid @RequestBody AnnouncementVisibilityUpdateRequest request){
        AnnouncementResponse response = announcementService.updateAnnouncementVisibility(announcementId, request);
        return ResponseEntity.status(200).body(createApiResponse("Announcement visibility updated successfully!", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{announcementId}")
    public ResponseEntity<ApiResponse<AnnouncementResponse>> updateAnnouncementTitleAndDescription(@PathVariable Long announcementId, @RequestBody AnnouncementUpdateRequest request){
        AnnouncementResponse response = announcementService.updateAnnouncement(announcementId, request);
        return ResponseEntity.status(200).body(createApiResponse("Announcement updated successfully!", response));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<ApiResponse<AnnouncementResponse>> archiveAnnouncement(@PathVariable Long id){
        AnnouncementResponse response = announcementService.archiveAnnouncementById(id);
        return ResponseEntity.status(200).body(createApiResponse("Announcement Archived successfully!", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{id}/pin")
    public ResponseEntity<ApiResponse<AnnouncementResponse>> pinAnnouncement(@PathVariable Long id){
        AnnouncementResponse response =  announcementService.pinAnnouncementById(id);
        return ResponseEntity.status(200).body(createApiResponse("Announcement Pin successfully!", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{id}/unpin")
    public ResponseEntity<ApiResponse<AnnouncementResponse>> unpinAnnouncement(@PathVariable Long id){
        AnnouncementResponse response =  announcementService.unpinAnnouncementById(id);
        return ResponseEntity.status(200).body(createApiResponse("Announcement Unpin successfully!", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<AnnouncementResponse>> publishAnnouncement(@PathVariable Long id){
        AnnouncementResponse response =  announcementService.publishAnnouncementById(id);
        return ResponseEntity.status(200).body(createApiResponse("Announcement published successfully!", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<AnnouncementResponse>> deleteAnnouncement(@PathVariable Long id){
        AnnouncementResponse response =  announcementService.deleteAnnouncementById(id);
        return ResponseEntity.status(200).body(createApiResponse("Announcement deleted successfully!", response));
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AnnouncementResponse>> getAnnouncementById(@PathVariable Long id){
        AnnouncementResponse response =  announcementService.getAnnouncementById(id);
        return ResponseEntity.status(200).body(createApiResponse("Announcement Found!", response));
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping
    public ResponseEntity<PaginatedApiResponse<AnnouncementResponse>> getAllAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "published_date") List<String> sortBy,
            AnnouncementFilterRequest filters
            ){
        Page<AnnouncementResponse> pageData = announcementService.getAllAnnouncements(page,size,direction,sortBy,filters);
        return ResponseEntity.status(200).body(createPaginatedApiResponse("Announcements fetched successfully!", pageData));
    }
}
