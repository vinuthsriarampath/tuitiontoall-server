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

import edu.vinu.entity.TeacherVacancyEntity;
import edu.vinu.enums.TeacherVacancyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherVacancyRepository extends JpaRepository<TeacherVacancyEntity, Long> {
    Page<TeacherVacancyEntity> findByInstituteId(Long instituteId, Pageable pageable);

    List<TeacherVacancyEntity> findByStatusAndInstituteId(TeacherVacancyStatus status, Long instituteId);

    Optional<TeacherVacancyEntity> findByIdAndStatus(Long vacancyId, TeacherVacancyStatus status);
}
