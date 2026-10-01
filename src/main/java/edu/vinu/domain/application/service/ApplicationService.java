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

package edu.vinu.domain.application.service;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.application.dto.Application;
import edu.vinu.domain.application.entity.ApplicationEntity;
import edu.vinu.domain.application.request.TeacherApplicationFilterRequest;
import edu.vinu.domain.application.response.ApplicationDetailsResponse;
import edu.vinu.domain.application.response.TeacherApplicationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ApplicationService {
    Application createApplication(Long vacancyId);

    boolean isUserAlreadyApplied(Long userId,Long vacancyId);

    Page<ApplicationDetailsResponse> getApplicationsByVacancy(Long vacancyId, int page, int size, String direction, String sortBy);

    List<ApplicationEntity> getAllApplicationEntitiesByIds(List<Long> applicationIds);

    void setApplicationStatusSelected(ApplicationEntity applicationEntity);

    void setApplicationStatusRejected(ApplicationEntity applicationEntity);

    PaginatedApiResponse<TeacherApplicationResponse> getCurrentTeacherApplications(PaginationRequest pagination, TeacherApplicationFilterRequest filters);

}
