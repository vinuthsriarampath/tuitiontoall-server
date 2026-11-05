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

import edu.vinu.domain.messaging.enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationResponse{
        private Long id;
        private String topic;
        private ConversationType type;
        private LocalDateTime lastMessageAt;
        private String otherParticipantName;
        private String otherParticipantSlug;
        private String otherParticipantDp;
        @Builder.Default
        private Long unreadCount = 0L;
}
