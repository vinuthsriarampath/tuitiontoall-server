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

package edu.vinu.infastructure.service.websocket.impl;

import edu.vinu.domain.user.service.UserService;
import edu.vinu.infastructure.service.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {
    private final UserService userService;
    private final SimpUserRegistry simpUserRegistry;
    @Override
    public boolean isUserSubscribed(Long userId, String destination) {
        ;
        SimpUser simpUser = simpUserRegistry.getUser(userService.getUserEntityById(userId).getEmail());
        if(simpUser != null){
            for (var session : simpUser.getSessions()) {
                for (SimpSubscription sub : session.getSubscriptions()) {
                    if (sub.getDestination().equals(destination)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
