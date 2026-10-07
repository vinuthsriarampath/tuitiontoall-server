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

package edu.vinu.domain.post.repository;

import edu.vinu.domain.post.entity.Post;
import edu.vinu.domain.post.repository.projections.UserPostProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Query(value = """
    SELECT
    p.id AS id,
    p.caption AS caption,
    p.status AS status,
    p.visibility AS visibility,
    p.created_date AS createdDate,
    p.updated_date AS updatedDate,
    p.published_date AS publishedDate,
    0 As likesCount,
    0 As commentsCount,
    0 AS isLikedByUser
    FROM post p
    WHERE p.user_id = :userId
    AND (:status IS NULL OR p.status = :status)
    AND (:visibility IS NULL OR p.visibility = :visibility)
    """,
    countQuery = """
    SELECT COUNT(*) FROM post p
    WHERE p.user_id = :userId
    AND (:status IS NULL OR p.status = :status)
    AND (:visibility IS NULL OR p.visibility = :visibility)
    """,nativeQuery = true)
    Page<UserPostProjection> findAllByOwner(Long userId, String status, String visibility, Pageable pageable);

    @Query(value = """
    SELECT IF(COUNT(*) > 0, true, false)
    FROM post p
    WHERE p.id = :postId AND p.user_id = :userId
    """,nativeQuery = true)
    int isPostOwner(Long postId, Long userId);

    @Query(value = """
    SELECT
    p.id AS id,
    p.caption AS caption,
    p.status AS status,
    p.visibility AS visibility,
    p.created_date AS createdDate,
    p.updated_date AS updatedDate,
    p.published_date AS publishedDate,
    0 As likesCount,
    0 As commentsCount,
    0 AS isLikedByUser
    FROM post p
    WHERE p.user_id = :targetUserId
    AND p.status = 'PUBLISHED'
    AND (
        p.visibility = 'PUBLIC'
        OR (:isFollowing = true AND p.visibility = 'FOLLOWERS_ONLY')
    )
    """,
    countQuery = """
    SELECT COUNT(*) FROM post p
    WHERE p.user_id = :targetUserId
    AND p.status = 'PUBLISHED'
    AND (
        p.visibility = 'PUBLIC'
        OR (:isFollowing = true AND p.visibility = 'FOLLOWERS_ONLY')
    )
    """, nativeQuery = true)
    Page<UserPostProjection> findTargetUserPosts(Long targetUserId, boolean isFollowing, Pageable pageable);
}
