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

import edu.vinu.domain.messaging.dtos.response.ConversationResponse;
import edu.vinu.domain.messaging.entity.Conversation;
import edu.vinu.domain.messaging.entity.ConversationParticipant;
import edu.vinu.domain.messaging.enums.ConversationType;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.helper.UserHelper;

public class ConversationMapper {

    public static ConversationResponse toConversationResponse(Conversation conv, UserEntity currentUser){

        String otherName = null;
        String otherSlug = null;
        String otherDp = null;

        // If it's a DIRECT chat, find the other participant's details for UI rendering
        if (conv.getType().equals(ConversationType.DIRECT)) {
            ConversationParticipant otherParticipant = conv.getParticipants().stream()
                    .filter(p -> !p.getUser().getId().equals(currentUser.getId()))
                    .findFirst()
                    .orElse(null);

            if (otherParticipant != null && otherParticipant.getUser() != null) {
                UserEntity otherUser = otherParticipant.getUser();
                otherName = UserHelper.getUserDisplayName(otherUser);
                otherSlug = otherUser.getUserSlug();
                otherDp = otherUser.getDp();
            }
        }

        return ConversationResponse.builder()
                .id(conv.getId())
                .topic(conv.getTopic())
                .type(conv.getType())
                .lastMessageAt(conv.getLastMessageAt())
                .otherParticipantName(otherName)
                .otherParticipantSlug(otherSlug)
                .otherParticipantDp(otherDp)
                .build();
    }
}
