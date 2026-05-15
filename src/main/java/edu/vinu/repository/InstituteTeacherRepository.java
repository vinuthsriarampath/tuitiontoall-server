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

package edu.vinu.repository;

import edu.vinu.entity.InstituteTeacherEntity;
import edu.vinu.repository.projection.InstituteTeacherProjection;
import edu.vinu.repository.projection.InstituteTeacherStatsProjection;
import edu.vinu.repository.projection.TeacherProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstituteTeacherRepository extends JpaRepository<InstituteTeacherEntity, Long> {
    boolean existsByTeacherIdAndInstituteId(Long teacherId, Long instituteId);

    @Query(value = """
            SELECT
            ti.id AS id,
            ti.status AS status,
            ti.institute_id AS instituteId,
            ti.joined_date AS joinedDate,
            ti.last_modified_date As lastModifiedDate,
            
            u.id AS userId,
            u.email AS email,
            u.contact As contact,
            u.dp As dp,
            u.address AS address,
            
            t.first_name AS firstName,
            t.last_name AS lastName,
            t.dob AS dob
            
            FROM institute_teacher ti
            JOIN teacher t ON ti.teacher_id = t.id
            JOIN users u ON t.user_id = u.id
            WHERE ti.institute_id = :instituteId
            
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM institute_teacher ti
                    WHERE ti.institute_id = :instituteId
                    """,
            nativeQuery = true)
    Page<InstituteTeacherProjection> getAllByInstituteId(@Param("instituteId") Long instituteId, Pageable pageable);

    @Query(value = """
                SELECT
                    COUNT(*) AS totalTeachers,
                    SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS activeTeachers,
                    SUM(CASE WHEN status = 'INACTIVE' THEN 1 ELSE 0 END) AS inactiveTeachers,
                    SUM(CASE WHEN status = 'SUSPENDED' THEN 1 ELSE 0 END) AS suspendedTeachers
                 FROM institute_teacher it
                 where it.institute_id = :instituteId
            """, nativeQuery = true)
    InstituteTeacherStatsProjection getInstituteTeacherStatsByInstituteId(@Param("instituteId") Long instituteId);

    @Query(
            value = """
                SELECT
                    t.id AS teacherId,
                    t.first_name AS firstName,
                    t.last_name AS lastName,
                FROM institute_teacher it
                JOIN teacher t ON it.teacher_id = t.id
                WHERE it.institute_id = :instituteId
            """,nativeQuery = true
    )
    List<TeacherProjection> findAllTeachersByInstituteId(@Param("instituteId") Long instituteId);

    Optional<InstituteTeacherEntity> findByTeacherIdAndInstituteId(Long id, Long instituteId);
}
