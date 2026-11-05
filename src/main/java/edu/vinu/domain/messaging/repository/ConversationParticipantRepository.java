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

import edu.vinu.domain.messaging.entity.ConversationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {

    /**
     * Find all participants of a specific conversation room.
     */
    @Query(value = """
        SELECT * FROM conversation_participant cp
        WHERE cp.conversation_id = :conversationId
        """, nativeQuery = true)
    List<ConversationParticipant> findByConversationId(@Param("conversationId") Long conversationId);

    /**
     * Check if a user is a participant in a specific conversation room.
     */
    @Query(value = """
        SELECT IF(COUNT(*) > 0, true, false)
        FROM conversation_participant cp
        WHERE cp.conversation_id = :conversationId AND cp.users_id = :userId
        """, nativeQuery = true)
    int isUserInConversation(@Param("conversationId") Long conversationId, @Param("userId") Long userId);
}
