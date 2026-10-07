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

import edu.vinu.domain.post.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
    @Query(value = """
    SELECT * FROM post_media pm
    WHERE pm.post_id = :postId
    ORDER BY pm.file_order
    """,nativeQuery = true)
    List<PostMedia> findAllByPostId(Long postId);


    @Modifying
    @Query(value = """
    DELETE FROM post_media pm
    WHERE pm.post_id = :postId
    """,nativeQuery = true)
    void deleteByPostId(Long postId);
}
