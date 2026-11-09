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

import edu.vinu.domain.messaging.entity.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, Long> {
    /**
     * Fetch all file attachments for a specific message.
     */
    @Query(value = """
        SELECT *
        FROM message_attachment ma
        WHERE ma.message_id = :messageId
        """, nativeQuery = true)
    List<MessageAttachment> findByMessageIdNative(Long messageId);

    @Query(value = """
            SELECT *
            FROM message_attachment ma
            WHERE ma.file_url = :filename
            """, nativeQuery = true)
    Optional<MessageAttachment> findByFileUrl(String filename);
}
