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

package edu.vinu.domain.messaging.dtos.response;

import edu.vinu.domain.messaging.enums.MessageDeliveryStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ChatMessageResponse(
        Long id,
        Long conversationId,
        Long senderId,
        String senderName,
        String senderDp,
        String content,
        MessageDeliveryStatus deliveryStatus,
        Boolean isEdited,
        Boolean isDeleted,
        LocalDateTime sentAt,
        List<MessageAttachmentResponse> attachments
) {
}
