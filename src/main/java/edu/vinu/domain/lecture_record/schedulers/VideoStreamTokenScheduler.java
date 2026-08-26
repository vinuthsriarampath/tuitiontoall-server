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

package edu.vinu.domain.lecture_record.schedulers;

import edu.vinu.domain.lecture_record.entity.VideoStreamTokenEntity;
import edu.vinu.domain.lecture_record.service.VideoStreamTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoStreamTokenScheduler {
    private final VideoStreamTokenService videoStreamTokenService;

    @Scheduled(cron = "0 */30 * * * *",zone = "Asia/Colombo")
    private void cleanExpiredTokens(){
        Map<String, VideoStreamTokenEntity> tokens = videoStreamTokenService.getAllTokens();

        int removedTokens = 0;

        for(Map.Entry<String, VideoStreamTokenEntity> entry : tokens.entrySet()){
            if (entry.getValue().getExpiresAt().isBefore(LocalDateTime.now())) {
                tokens.remove(entry.getKey());
                removedTokens++;
            }
        }
        log.atInfo().log("Removed {} expired tokens at {}", removedTokens,  LocalDateTime.now());
    }
}
