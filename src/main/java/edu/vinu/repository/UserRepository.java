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

import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.entity.user_entities.StudentEntity;
import edu.vinu.entity.user_entities.TeacherEntity;
import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.enums.Role;
import edu.vinu.filter.repository_filter.FilteredRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>, FilteredRepositoryCustom{
    UserEntity findByEmail(String username);

    @Query("SELECT i FROM InstituteEntity i WHERE i.email = :email AND i.isDisabled = false")
    InstituteEntity findInstituteByEmail(@Param("email") String email);

    @Query("SELECT s FROM StudentEntity s WHERE s.email = :email AND s.isDisabled = false")
    StudentEntity findStudentByEmail(@Param("email") String email);

    @Query("SELECT t FROM TeacherEntity t WHERE t.email = :email AND t.isDisabled = false")
    TeacherEntity findTeacherByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    @Query("SELECT s from StudentEntity s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT(:firstName, '%')) AND s.isDisabled = false")
    List<StudentEntity> getStudentsByFirstNameLike(@Param("firstName") String firstName);

    @Query("SELECT t FROM TeacherEntity t WHERE LOWER(t.firstName) LIKE LOWER(CONCAT(:firstName, '%')) AND t.isDisabled = false")
    List<TeacherEntity> getTeachersByFirstNameLike(@Param("firstName") String firstName);

    List<UserEntity> findAllByRole(Role role);

    @Query("SELECT i FROM InstituteEntity i WHERE i.role = 'ROLE_INSTITUTE' AND i.isDisabled = false")
    List<InstituteEntity> getAllInstitutes();

    @Query("SELECT t FROM TeacherEntity t WHERE t.role = 'ROLE_TEACHER' AND t.isDisabled = false")
    List<TeacherEntity> getAllTeachers();

    @Query("SELECT s FROM StudentEntity s WHERE s.role = 'ROLE_STUDENT' AND s.isDisabled = false")
    List<StudentEntity> getAllStudents();

    @Query("SELECT i FROM InstituteEntity i WHERE LOWER(i.instituteName) LIKE LOWER(CONCAT('%', :instituteName, '%')) AND i.isDisabled = false")
    List<InstituteEntity> findByInstituteName(@Param("instituteName") String instituteName);
}
