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

package edu.vinu.service.common;

import edu.vinu.enums.TeacherVacancyStatus;
import edu.vinu.model.TeacherVacancy;
import edu.vinu.request.CreateVacancyRequest;
import edu.vinu.request.UpdateVacancyRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeacherVacancyService {
    TeacherVacancy createVacancy(CreateVacancyRequest request);

    TeacherVacancy updateVacancy(Long vacancyId, UpdateVacancyRequest request);

    void deleteVacancy(Long vacancyId);

    TeacherVacancy getById(Long vacancyId);

    Page<TeacherVacancy> getAllByInstitute(int page, int size, String sortBy, String direction);

    List<TeacherVacancy> getAllByInstituteIdAndStatus(Long instituteId, String  status);
}
