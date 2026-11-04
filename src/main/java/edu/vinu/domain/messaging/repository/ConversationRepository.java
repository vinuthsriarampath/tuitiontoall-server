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

import edu.vinu.domain.messaging.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    /**
     * Fetch all conversation rooms a specific user belongs to,
     * sorted by the most recent message activity.
     */
    @Query(value = """
    SELECT c.*
    FROM conversation c
    JOIN conversation_participant cp ON c.id = cp.conversation_id
    WHERE cp.users_id = :userId
    ORDER BY c.last_message_at DESC
    """,nativeQuery = true)
    List<Conversation> findAllByUserId(Long userId);


    /**
     * check if a direct 1-to-1 conversation already exists between two users.
     */
    @Query(value = """
        SELECT c.*
        FROM conversation c
        JOIN conversation_participant cp1 ON c.id = cp1.conversation_id
        JOIN conversation_participant cp2 ON c.id = cp2.conversation_id
        WHERE c.type = 'DIRECT'
          AND cp1.users_id = :user1Id
          AND cp2.users_id = :user2Id
        LIMIT 1
        """, nativeQuery = true)
    Conversation findDirectConversationBetweenUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}
