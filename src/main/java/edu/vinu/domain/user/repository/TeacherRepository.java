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

package edu.vinu.domain.user.repository;

import edu.vinu.domain.user.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity,Long> {
    @Query(value = "SELECT t.* FROM teacher t JOIN users u ON u.id = t.user_id WHERE u.is_disabled = false",nativeQuery = true)
    List<TeacherEntity> getAllTeachers();

    @Query(value = "SELECT t.* FROM teacher t JOIN users u ON u.id = t.user_id WHERE LOWER(t.first_name) LIKE LOWER(CONCAT(:firstName, '%')) AND u.is_disabled = false",nativeQuery = true)
    List<TeacherEntity> getTeachersByFirstNameLike(@Param("firstName") String firstName);

    @Query(value = "SELECT t.* FROM teacher t JOIN users u ON u.id = t.user_id WHERE u.email = :email AND u.is_disabled = false",nativeQuery = true)
    Optional<TeacherEntity> findTeacherByEmail(@Param("email") String email);
}
