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

package edu.vinu.domain.lecture_record.service.impl;

import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.domain.lecture_record.entity.VideoStreamTokenEntity;
import edu.vinu.domain.lecture_record.service.VideoStreamTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VideoStreamTokenServiceImpl implements VideoStreamTokenService {

    private final Map<String, VideoStreamTokenEntity> tokens = new ConcurrentHashMap<>();

    @Override
    public String generateToken(String fileName) {
        String token = UUID.randomUUID().toString();

        VideoStreamTokenEntity streamToken = VideoStreamTokenEntity.builder()
                        .token(token)
                        .fileName(fileName)
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusHours(24)
                        )
                        .build();

        tokens.put(token, streamToken);
        return token;
    }

    @Override
    public VideoStreamTokenEntity validateToken(String token) {
        VideoStreamTokenEntity streamToken = tokens.get(token);

        if(streamToken == null){
            throw new UnauthorizedException("Invalid stream token");
        }

        if(streamToken.getExpiresAt().isBefore(LocalDateTime.now())){
            tokens.remove(token);
            throw new UnauthorizedException("Stream token expired");
        }

        return streamToken;
    }
}
