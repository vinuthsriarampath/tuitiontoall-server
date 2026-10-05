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

package edu.vinu.domain.user_follow.repository;

import edu.vinu.domain.user.repository.projection.UserBasicProjection;
import edu.vinu.domain.user_follow.entity.UserFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    @Query(value = """
    SELECT EXISTS(
        SELECT 1
        FROM user_follow uf
        WHERE uf.follower_id = :followerId AND uf.following_id = :followingId
    )
    """,nativeQuery = true)
    int existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query(value = """
    SELECT *
    FROM user_follow uf
    WHERE uf.follower_id = :followerId AND uf.following_id = :followingId
    """,nativeQuery = true)
    Optional<UserFollow> getByFollowerAndFollowingId(Long followerId, Long followingId);

    @Query(value = """
    SELECT COUNT(*)
    FROM user_follow uf
    WHERE uf.following_id = :userId
    """,nativeQuery = true)
    long countFollowersByUserId(Long userId);

    @Query(value = """
    SELECT COUNT(*)
    FROM user_follow uf
    WHERE uf.follower_id = :userId
    """,nativeQuery = true)
    long countFollowingByUserId(Long userId);

    @Query(value = """
    SELECT
        u.id AS id,
        u.user_slug AS userSlug,
        u.dp AS dp,
        u.email AS email,
        r.role AS role,
        CASE
        	WHEN r.role = 'student' THEN CONCAT(s.first_name, ' ', s.last_name)
        	WHEN r.role = 'teacher' THEN CONCAT(t.first_name, ' ', t.last_name)
        	WHEN r.role = 'institute' THEN i.institute_name
        	ELSE u.email
        END AS displayName
    FROM user_follow uf
    INNER JOIN users u ON uf.follower_id = u.id
    INNER JOIN role r ON u.role_id = r.id
    LEFT JOIN student s ON s.user_id = u.id AND r.role = 'student'
    LEFT JOIN teacher t ON t.user_id = u.id AND r.role = 'teacher'
    LEFT JOIN institute i ON i.user_id = u.id AND r.role = 'institute'
    WHERE uf.following_id = :followingId
    AND u.is_disabled = false
    AND (:userId IS NULL OR u.id = :userId)
    AND (:email IS NULL OR  :email = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
    AND (:userSlug IS NULL OR :userSlug = '' OR LOWER(u.user_slug) LIKE LOWER(CONCAT('%', :userSlug, '%')))
    AND (:role IS NULL OR LOWER(r.role) = LOWER(:role))
    AND (
        :displayName IS NULL OR :displayName = ''
        OR (
            (r.role = 'student' AND LOWER(CONCAT(s.first_name, ' ', s.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
            OR (r.role = 'teacher' AND LOWER(CONCAT(t.first_name, ' ', t.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
            OR (r.role = 'institute' AND LOWER(i.institute_name) LIKE LOWER(CONCAT('%', :displayName, '%')))
        )
    )
    ORDER BY uf.followed_on desc
    """,
    countQuery = """
    SELECT COUNT(*)
    FROM user_follow uf
    INNER JOIN users u ON uf.follower_id = u.id
    INNER JOIN role r ON u.role_id = r.id
    LEFT JOIN student s ON s.user_id = u.id AND r.role = 'student'
    LEFT JOIN teacher t ON t.user_id = u.id AND r.role = 'teacher'
    LEFT JOIN institute i ON i.user_id = u.id AND r.role = 'institute'
    WHERE uf.following_id = :followingId
    AND u.is_disabled = false
    AND (:userId IS NULL OR u.id = :userId)
    AND (:email IS NULL OR :email = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
    AND (:userSlug IS NULL OR :userSlug = '' OR LOWER(u.user_slug) LIKE LOWER(CONCAT('%', :userSlug, '%')))
    AND (:role IS NULL OR LOWER(r.role) = LOWER(:role))
    AND (
        :displayName IS NULL OR :displayName = ''
        OR (
            (r.role = 'student' AND LOWER(CONCAT(s.first_name, ' ', s.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
            OR (r.role = 'teacher' AND LOWER(CONCAT(t.first_name, ' ', t.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
            OR (r.role = 'institute' AND LOWER(i.institute_name) LIKE LOWER(CONCAT('%', :displayName, '%')))
        )
    )
    ORDER BY uf.followed_on desc
    """,nativeQuery = true)
    Page<UserBasicProjection> getFollowersByFollowingId(Long followingId, Long userId, String displayName, String email,String userSlug, String role, Pageable pageable);

    @Query(value = """
    SELECT
        u.id AS id,
        u.user_slug AS userSlug,
        u.dp AS dp,
        u.email AS email,
        r.role AS role,
        CASE
        	WHEN r.role = 'student' THEN CONCAT(s.first_name, ' ', s.last_name)
        	WHEN r.role = 'teacher' THEN CONCAT(t.first_name, ' ', t.last_name)
        	WHEN r.role = 'institute' THEN i.institute_name
        	ELSE u.email
        END AS displayName
    FROM user_follow uf
    INNER JOIN users u ON uf.following_id = u.id
    INNER JOIN role r ON u.role_id = r.id
    LEFT JOIN student s ON s.user_id = u.id AND r.role = 'student'
    LEFT JOIN teacher t ON t.user_id = u.id AND r.role = 'teacher'
    LEFT JOIN institute i ON i.user_id = u.id AND r.role = 'institute'
    WHERE uf.follower_id = :followerId
    AND u.is_disabled = false
    AND (:userId IS NULL OR u.id = :userId)
    AND (:email IS NULL OR :email = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
    AND (:userSlug IS NULL OR :userSlug = '' OR LOWER(u.user_slug) LIKE LOWER(CONCAT('%', :userSlug, '%')))
    AND (:role IS NULL OR LOWER(r.role) = LOWER(:role))
    AND (
        :displayName IS NULL OR :displayName = ''
        OR (
            (r.role = 'student' AND LOWER(CONCAT(s.first_name, ' ', s.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
            OR (r.role = 'teacher' AND LOWER(CONCAT(t.first_name, ' ', t.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
            OR (r.role = 'institute' AND LOWER(i.institute_name) LIKE LOWER(CONCAT('%', :displayName, '%')))
        )
    )
    ORDER BY uf.followed_on desc
    """,
    countQuery = """
    SELECT COUNT(*)
    FROM user_follow uf
    INNER JOIN users u ON uf.following_id = u.id
    INNER JOIN role r ON u.role_id = r.id
    LEFT JOIN student s ON s.user_id = u.id AND r.role = 'student'
    LEFT JOIN teacher t ON t.user_id = u.id AND r.role = 'teacher'
    LEFT JOIN institute i ON i.user_id = u.id AND r.role = 'institute'
    WHERE uf.follower_id = :followerId
    AND u.is_disabled = false
    AND (:userId IS NULL OR u.id = :userId)
    AND (:email IS NULL OR :email = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
    AND (:userSlug IS NULL OR :userSlug = '' OR LOWER(u.user_slug) LIKE LOWER(CONCAT('%', :userSlug, '%')))
    AND (:role IS NULL OR LOWER(r.role) = LOWER(:role))
    AND (
        :displayName IS NULL OR :displayName = ''
        OR (
            (r.role = 'student' AND LOWER(CONCAT(s.first_name, ' ', s.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
            OR (r.role = 'teacher' AND LOWER(CONCAT(t.first_name, ' ', t.last_name)) LIKE LOWER(CONCAT('%', :displayName, '%')))
            OR (r.role = 'institute' AND LOWER(i.institute_name) LIKE LOWER(CONCAT('%', :displayName, '%')))
        )
    )
    ORDER BY uf.followed_on desc
    """, nativeQuery = true)
    Page<UserBasicProjection> getFollowingsByFollowerId(Long followerId, Long userId, String displayName, String email,String userSlug, String role, Pageable pageable);
}
