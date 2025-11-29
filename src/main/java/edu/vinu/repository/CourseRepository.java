/*
 * Copyright (c) 2025 vinuth sri arampath
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

package edu.vinu.repository;

import edu.vinu.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    List<CourseEntity> findAllByInstituteId(Long id);

    @Query(value = """
                    SELECT *
                    FROM courses c
                    WHERE c.institute_id = :instituteId
                    AND (:category IS NULL OR c.category = :category)
                    AND (:level IS NULL OR c.level = :level)
                    AND (:language IS NULL OR c.language = :language)
                    AND (:mode IS NULL OR c.mode = :mode)
                    AND (:status IS NULL OR c.status = :status);
            """, nativeQuery = true)
    List<CourseEntity> findAllByInstituteIdWithFilters(@Param("instituteId") Long instituteId, @Param("category") String category, @Param("level") String level, @Param("language") String language, @Param("mode") String mode, @Param("status") String status);
}
