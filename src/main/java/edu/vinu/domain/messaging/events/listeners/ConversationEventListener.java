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

package edu.vinu.domain.messaging.events.listeners;

import edu.vinu.domain.messaging.events.ConversationMessageReceivedEvent;
import edu.vinu.domain.messaging.events.ConversationReadEvent;
import edu.vinu.domain.messaging.websocket.ConversationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConversationEventListener {

    private final ConversationEventPublisher eventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageReceived(ConversationMessageReceivedEvent event) {
        try {
            eventPublisher.publishMessageReceived(event.recipientId(), event.conversationId(), event.messageId());
        } catch (Exception exception) {
            log.error(
                    "Failed to publish MESSAGE_RECEIVED event for conversation {} to user {}",
                    event.conversationId(),
                    event.recipientId(),
                    exception
            );
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleConversationRead(ConversationReadEvent event) {
        try {
            eventPublisher.publishConversationRead(event.recipientId(), event.conversationId());
        } catch (Exception exception) {
            log.error(
                    "Failed to publish CONVERSATION_READ event for conversation {} to user {}",
                    event.conversationId(),
                    event.recipientId(),
                    exception
            );
        }
    }
}
