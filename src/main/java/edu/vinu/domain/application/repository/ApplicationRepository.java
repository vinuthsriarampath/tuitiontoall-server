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

package edu.vinu.domain.application.repository;

import edu.vinu.domain.application.entity.ApplicationEntity;
import edu.vinu.repository.projection.ApplicationProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    @Query(value = """ 
            SELECT EXISTS(
                SELECT 1
                FROM applications a
                WHERE a.teacher_id = :teacherId AND a.teacher_vacancy_id = :vacancyId
            )
            """, nativeQuery = true)
    Integer isUserAlreadyApplied(@Param("teacherId") Long teacherId, @Param("vacancyId") Long vacancyId);


    @Query(value = """ 
            SELECT
                                    a.id AS id,
                                    a.teacher_vacancy_id AS teacherVacancyId,
                                    a.status AS status,
                                    a.applied_date AS appliedDate,
                                    a.last_modified_date AS lastModifiedDate,
                                    u.id AS userId,
                                    u.email AS email,
                                    u.contact AS contact,
                                    u.dp AS dp,
                                    u.address AS address,
                                    t.first_name AS firstName,
                                    t.last_name AS lastName,
                                    t.dob AS dob
                                    FROM applications a
                                    JOIN teacher t ON a.teacher_id = t.id
                                    JOIN users u ON t.user_id = u.id
                                    WHERE a.teacher_vacancy_id = :vacancyId
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM applications a
                    WHERE a.teacher_vacancy_id = :vacancyId
                    """,
            nativeQuery = true
    )
    Page<ApplicationProjection> findAllByTeacherVacancy_Id(@Param("vacancyId") Long teacherVacancyId, Pageable pageable);
}
