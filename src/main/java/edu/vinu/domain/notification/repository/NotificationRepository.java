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

package edu.vinu.domain.notification.repository;

import edu.vinu.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Fetch paginated notifications for a specific user,
     * ordered by newest creation date first.
     */
    @Query(
            value = """
                    SELECT * FROM notifications n
                    WHERE n.user_id = :userId
                    ORDER BY n.created_at DESC
                    """,
            countQuery = """
                    SELECT COUNT(*) FROM notifications n
                    WHERE n.user_id = :userId
                    """,
            nativeQuery = true
    )
    Page<Notification> findAllByUserId(Long userId, Pageable pageable);

    /**
     * Count unread notifications for a user.
     */
    @Query(
            value = """
                    SELECT COUNT(*) FROM notifications n
                    WHERE n.user_id = :userId AND n.is_read = 0
                    """,
            nativeQuery = true
    )
    long countUnreadByUserId(Long userId);

    /**
     * Mark a single notification as read, ensuring it belongs to the user.
     */
    @Transactional
    @Modifying
    @Query(
            value = """
                    UPDATE notifications n
                    SET n.is_read = 1, n.read_at = CURRENT_TIMESTAMP
                    WHERE n.id = :notificationId AND n.user_id = :userId
                    """,
            nativeQuery = true
    )
    int markAsRead(Long notificationId, Long userId);

    /**
     * Mark all unread notifications as read for a specific user.
     */
    @Transactional
    @Modifying
    @Query(
            value = """
                    UPDATE notifications n
                    SET n.is_read = 1, n.read_at = CURRENT_TIMESTAMP
                    WHERE n.user_id = :userId AND n.is_read = 0
                    """,
            nativeQuery = true
    )
    int markAllAsRead(Long userId);

    /**
     * Fetch a notification by its ID and user ID for security validation.
     */
    @Query(
            value = """
                    SELECT * FROM notifications n
                    WHERE n.id = :notificationId AND n.user_id = :userId
                    """,
            nativeQuery = true
    )
    Notification findByIdAndUserId(Long notificationId, Long userId);
}
