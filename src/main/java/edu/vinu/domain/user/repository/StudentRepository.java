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

import edu.vinu.domain.student.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,Long> {

    @Query(value = "SELECT s.* FROM student s JOIN users u ON u.id = s.user_id WHERE u.is_disabled = false",nativeQuery = true)
    List<StudentEntity> getAllStudents();

    @Query(value = "SELECT s.* from student s JOIN users u ON u.id = s.user_id WHERE LOWER(s.first_name) LIKE LOWER(CONCAT(:firstName, '%')) AND u.is_disabled = false",nativeQuery = true)
    List<StudentEntity> getStudentsByFirstNameLike(@Param("firstName") String firstName);

    @Query(value = "SELECT s.* FROM student s JOIN users u ON u.id = s.user_id WHERE u.email = :email AND u.is_disabled = false",nativeQuery = true)
    Optional<StudentEntity> findStudentByEmail(@Param("email") String email);
}
