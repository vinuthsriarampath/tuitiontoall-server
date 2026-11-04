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

package edu.vinu.domain.messaging.mapper;

import edu.vinu.domain.messaging.dtos.response.ChatMessageResponse;
import edu.vinu.domain.messaging.dtos.response.MessageAttachmentResponse;
import edu.vinu.domain.messaging.entity.Message;

import java.util.List;

public class MessageMapper {
    public static ChatMessageResponse toChatMessageResponse(Message message){
        List<MessageAttachmentResponse> attachmentResponses = message.getAttachments().stream()
                .map(MessageAttachmentMapper::toMessageAttachmentResponse)
                .toList();

        return ChatMessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getUserSlug()) // Adjust to display name if available
                .senderDp(message.getSender().getDp())
                .content(message.getContent())
                .deliveryStatus(message.getDeliveryStatus())
                .isEdited(message.getIsEdited())
                .isDeleted(message.getIsDeleted())
                .sentAt(message.getSentAt())
                .attachments(attachmentResponses)
                .build();
    }
}
