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

package edu.vinu.domain.messaging.services;

import edu.vinu.domain.messaging.dtos.request.SendMessageRequest;
import edu.vinu.domain.messaging.dtos.response.ChatMessageResponse;
import edu.vinu.domain.messaging.enums.MessageDeliveryStatusUpdate;

import java.util.List;

public interface MessagingService {
    ChatMessageResponse saveAndProcessMessage(String senderEmail, SendMessageRequest request);

    void updateMessagesStatus(Long aLong, List<Long> longs, MessageDeliveryStatusUpdate status);

    ChatMessageResponse deleteMessage(String senderEmail, Long messageId);
}
