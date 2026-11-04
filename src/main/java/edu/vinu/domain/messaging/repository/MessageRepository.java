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

package edu.vinu.domain.messaging.repository;

import edu.vinu.domain.messaging.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Fetch message history for a given conversation room ordered chronologically.
     */
    @Query(
            value = """
            SELECT * FROM message m
            WHERE m.conversation_id = :conversationId
              AND m.is_deleted = 0
            """,
            countQuery = """
            SELECT COUNT(*) FROM message m
            WHERE m.conversation_id = :conversationId
              AND m.is_deleted = 0
            """,
            nativeQuery = true
    )
    Page<Message> findMessagesByConversationId(Long conversationId, Pageable pageable);
}
