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

package edu.vinu.domain.feedback.repository;

import edu.vinu.domain.feedback.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query(value = """
    SELECT COUNT(*)
    FROM feedbacks f
    WHERE f.course_id = :courseId  AND f.user_id = :userId
    """, nativeQuery = true)
    int countUserFeedbacksByCourseId(Long courseId, Long userId);

    @Query(value = """
    SELECT *
        FROM feedbacks f
        WHERE f.course_id = :courseId
    """,
    countQuery = """
    SELECT COUNT(*)
        FROM feedbacks f
        WHERE f.course_id = :courseId
    """, nativeQuery = true)
    Page<Feedback> getFeedbacksByCourse(Long courseId, Pageable pageable);
}
