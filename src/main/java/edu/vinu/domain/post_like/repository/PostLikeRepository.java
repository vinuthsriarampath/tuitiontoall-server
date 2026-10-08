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

package edu.vinu.domain.post_like.repository;

import edu.vinu.domain.post_like.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Query(value = """
    SELECT IF(COUNT(*) > 0, true, false)
    FROM post_like pl
    WHERE pl.post_id = :postId AND pl.user_id = :userId
    LIMIT 1
    """,nativeQuery = true)
    int isLikedByUser(Long postId, Long userId);
}
