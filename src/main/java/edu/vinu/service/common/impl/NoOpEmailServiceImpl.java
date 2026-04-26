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

package edu.vinu.service.common.impl;

import edu.vinu.service.common.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@Profile("!prod")
public class NoOpEmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> templateVariables) throws MessagingException {
        log.info("Email disabled (non-prod)");
    }

    @Override
    public void SendRegistrationSuccessEmail(String email, String name, String role) {
        log.info("Email disabled (non-prod). Skipping SendRegistrationSuccessEmail to {}", email);
    }

    @Override
    public void SendPasswordResetEmail(String email, String name, String Link) {
        log.info("Email disabled (non-prod). Skipping SendPasswordResetEmail to {}", email);
    }

    @Override
    public void SendApplicationSuccessEmail(String email, String name, String link) {
        log.info("Email disabled (non-prod). Skipping SendApplicationSuccessEmail to {}", email);
    }
}
