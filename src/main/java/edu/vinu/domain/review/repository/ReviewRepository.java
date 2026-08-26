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

package edu.vinu.domain.review.repository;

import edu.vinu.domain.review.entity.Review;
import edu.vinu.domain.review.repository.projector.BasicReviewProjector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query(value = """
    SELECT
        r.id AS id,
        r.review AS review,
        r.rating AS rating,
        r.created_date AS createdDate,
        r.last_modified_date AS lastModifiedDate
    FROM reviews r
    WHERE r.course_id = :courseId
    """,
    countQuery = """
    SELECT COUNT(*) FROM reviews r WHERE r.course_id = :courseId
    """, nativeQuery = true)
    Page<BasicReviewProjector> findReviewsByCourseId(Long courseId, Pageable pageable);


    @Query(value = """
    SELECT COUNT(*) FROM reviews r WHERE r.course_id = :courseId AND r.user_id = :userId
    """, nativeQuery = true)
    Long countUserReviewsByCourseId(Long courseId, Long userId);
}
