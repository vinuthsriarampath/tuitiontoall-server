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

package edu.vinu.domain.announcement.service;

import edu.vinu.domain.announcement.request.AnnouncementFilterRequest;
import edu.vinu.domain.announcement.request.create.AnnouncementCreateRequest;
import edu.vinu.domain.announcement.request.update.AnnouncementUpdateRequest;
import edu.vinu.domain.announcement.request.update.AnnouncementVisibilityUpdateRequest;
import edu.vinu.domain.announcement.response.AnnouncementResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AnnouncementService {

    AnnouncementResponse createAnnouncement(AnnouncementCreateRequest request);

    AnnouncementResponse updateAnnouncementVisibility(Long announcementId, AnnouncementVisibilityUpdateRequest request);

    Page<AnnouncementResponse> getAllAnnouncements(int page, int size, String direction, List<String> sortBy, AnnouncementFilterRequest filters);

    AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementUpdateRequest request);

    AnnouncementResponse archiveAnnouncementById(Long id);

    AnnouncementResponse pinAnnouncementById(Long id);

    AnnouncementResponse unpinAnnouncementById(Long id);

    AnnouncementResponse publishAnnouncementById(Long id);

    AnnouncementResponse deleteAnnouncementById(Long id);

    AnnouncementResponse getAnnouncementById(Long id);
}
