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

package edu.vinu.domain.announcement.schedulers;

import edu.vinu.domain.announcement.enums.AnnouncementStatus;
import edu.vinu.domain.announcement.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnouncementScheduler {

    private final AnnouncementRepository announcementRepository;

    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Colombo")
    private void expiredAnnouncementForTheTime(){
        int updated = announcementRepository.expireAnnouncementsByExpireAt(
                AnnouncementStatus.EXPIRED.name(),
                AnnouncementStatus.PUBLISHED.name()
        );
        log.atInfo().log("{} Announcements got EXPIRED! at {}",updated, LocalDateTime.now());
    }
}
