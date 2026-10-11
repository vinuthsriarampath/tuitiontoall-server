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

package edu.vinu.domain.post_comment.repository;

import edu.vinu.domain.post_comment.entity.PostComment;
import edu.vinu.domain.post_comment.repository.projection.PostCommentDetailedProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query(value = """
    SELECT
        pc.id AS id,
        u.id AS userId,
        CASE
        	WHEN r.role = 'student' THEN CONCAT(s.first_name, ' ', s.last_name)
        	WHEN r.role = 'teacher' THEN CONCAT(t.first_name, ' ', t.last_name)
        	WHEN r.role = 'institute' THEN i.institute_name
        	ELSE u.email
        END AS displayName,
        u.dp AS dp,
        u.user_slug AS userSlug,
        pc.message AS message,
        pc.created_date AS createdDate,
        pc.modified_date AS updatedDate
    FROM post_comments pc
    JOIN users u ON pc.user_id = u.id
    LEFT JOIN role r ON u.role_id = r.id
    LEFT JOIN institute i ON i.user_id = u.id and r.role = 'institute'
    LEFT JOIN student s ON s.user_id = u.id and r.role = 'student'
    LEFT JOIN teacher t ON t.user_id = u.id and r.role = 'teacher'
    WHERE pc.post_id = :postId
    AND (:userId IS NULL OR pc.user_id = :userId)
    AND ( :displayName IS NULL OR :displayName = ''
        OR (
                (r.role = 'institute' AND LOWER(i.institute_name) LIKE LOWER(CONCAT('%', :displayName, '%')))
                OR (r.role = 'student' AND LOWER(CONCAT(s.first_name, ' ', s.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
                OR (r.role = 'teacher' AND LOWER(CONCAT(t.first_name, ' ', t.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
        )
    )
    """,countQuery = """
    
    """,nativeQuery = true)
    Page<PostCommentDetailedProjection> findAllByPostId(Long postId, Long userId, String displayName, Pageable pageable);
}
