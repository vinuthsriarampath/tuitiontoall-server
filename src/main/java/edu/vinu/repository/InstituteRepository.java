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

import edu.vinu.domain.institute.entity.InstituteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstituteRepository extends JpaRepository<InstituteEntity,Long> {

    @Query(value = "SELECT i.* FROM institute i JOIN users u ON i.user_id = u.id WHERE u.is_disabled = false",nativeQuery = true)
    List<InstituteEntity> getAllInstitutes();

    @Query(value = "SELECT i.* FROM institute i join users u on i.user_id = u.id WHERE LOWER(i.institute_name) LIKE LOWER(CONCAT('%', :instituteName, '%')) AND u.is_disabled = false",nativeQuery = true)
    List<InstituteEntity> findByInstituteName(@Param("instituteName") String instituteName);

    @Query(value = "SELECT i.* FROM institute i JOIN users u on u.id = i.user_id WHERE U.email = :email AND u.is_disabled = false",nativeQuery = true)
    Optional<InstituteEntity> findInstituteByEmail(@Param("email") String email);
}
