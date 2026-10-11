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
import edu.vinu.domain.post.repository.projections.FeedPostProjection;
import edu.vinu.domain.post.repository.projections.UserPostProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


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
    ( SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.id ) AS likesCount,
    ( SELECT COUNT(*) FROM post_comments pc WHERE pc.post_id = P.id) AS commentsCount,
    IF( EXISTS( SELECT 1 FROM post_like pl WHERE pl.post_id = p.id AND pl.user_id = :userId ), 1, 0) AS isLikedByUser
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
    ( SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.id ) AS likesCount,
    ( SELECT COUNT(*) FROM post_comments pc WHERE pc.post_id = P.id) AS commentsCount,
    IF( EXISTS( SELECT 1 FROM post_like pl WHERE pl.post_id = p.id AND pl.user_id = :currentUserId ), 1, 0) AS isLikedByUser
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
    Page<UserPostProjection> findTargetUserPosts(Long targetUserId,Long currentUserId, boolean isFollowing, Pageable pageable);


    @Query(value = """
        SELECT
            p.id AS id,
            p.caption AS caption,
            p.status AS status,
            p.visibility AS visibility,
            p.created_date AS createdDate,
            p.updated_date AS updatedDate,
            p.published_date AS publishedDate,
            u.id AS authorId,
            u.user_slug AS authorSlug,
            u.dp AS authorDp,
            r.role AS authorRole,
            CASE
                WHEN r.role = 'student' THEN CONCAT(s.first_name, ' ', s.last_name)
                WHEN r.role = 'teacher' THEN CONCAT(t.first_name, ' ', t.last_name)
                WHEN r.role = 'institute' THEN i.institute_name
                ELSE u.email
            END AS authorName,
            EXISTS(
                SELECT 1 FROM user_follow uf 
                WHERE uf.follower_id = :currentUserId AND uf.following_id = u.id
            ) AS isFollowingAuthor,
            (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.id) AS likesCount,
            (SELECT COUNT(*) FROM post_comments c WHERE c.post_id = p.id) AS commentsCount,
            EXISTS(
                SELECT 1 FROM post_like pl 
                WHERE pl.post_id = p.id AND pl.user_id = :currentUserId
            ) AS isLikedByUser

        FROM post p
        INNER JOIN users u ON p.user_id = u.id
        INNER JOIN role r ON u.role_id = r.id
        LEFT JOIN student s ON s.user_id = u.id AND r.role = 'student'
        LEFT JOIN teacher t ON t.user_id = u.id AND r.role = 'teacher'
        LEFT JOIN institute i ON i.user_id = u.id AND r.role = 'institute'

        WHERE u.is_disabled = false
          AND p.status = 'PUBLISHED'
          AND (
              p.user_id = :currentUserId
              OR (
                  EXISTS(
                      SELECT 1 FROM user_follow uf 
                      WHERE uf.follower_id = :currentUserId AND uf.following_id = p.user_id
                  )
                  AND p.visibility IN ('PUBLIC', 'FOLLOWERS_ONLY')
              )
              OR (p.visibility = 'PUBLIC')
          )
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM post p
        INNER JOIN users u ON p.user_id = u.id
        WHERE u.is_disabled = false
          AND p.status = 'PUBLISHED'
          AND (
              p.user_id = :currentUserId
              OR (
                  EXISTS(
                      SELECT 1 FROM user_follow uf
                      WHERE uf.follower_id = :currentUserId AND uf.following_id = p.user_id
                  )
                  AND p.visibility IN ('PUBLIC', 'FOLLOWERS_ONLY')
              )
              OR (p.visibility = 'PUBLIC')
          )
        """, nativeQuery = true)
    Page<FeedPostProjection> findFeedPosts(Long currentUserId, Pageable pageable);
}
