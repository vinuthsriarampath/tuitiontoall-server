/*
 * Copyright (c) 2025 vinuth sri arampath
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

import edu.vinu.enums.Role;
import edu.vinu.service.common.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> templateVariables) throws MessagingException {
        Context context =  new Context();
        context.setVariables(templateVariables);

        String htmlContent = templateEngine.process(templateName,context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("vinuthsriarampth@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        try{
            javaMailSender.send(message);
        }catch (MailException ex){
            log.error("Failed to send email",ex);
        }

    }

    @Override
    public void SendRegistrationSuccessEmail(String email, String name, Role role) {
        try{

            Map<String, Object> templateVariables = new HashMap<>();
            templateVariables.put("name",name);
            templateVariables.put("role", role);

            sendEmail(email,"Welcome To TuitionToAll","registration-success",templateVariables);
        }catch (MessagingException ex){
            log.error("Error sending registration success email to {}: {}", email, ex.getMessage());
            throw new RuntimeException("Failed to send registration success email", ex);
        }
    }
}
