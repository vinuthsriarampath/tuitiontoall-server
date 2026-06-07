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

package edu.vinu.domain.institute.service;

import edu.vinu.domain.application.response.ApplicationRejectionResponse;
import edu.vinu.domain.application.response.ApplicationSelectionResponse;
import edu.vinu.domain.institute.response.InstituteTeacherResponse;
import edu.vinu.domain.institute.response.InstituteTeacherStatsResponse;
import edu.vinu.domain.user.entity.TeacherEntity;
import edu.vinu.domain.user.response.TeacherBasicResponse;
import edu.vinu.domain.application.request.ApplicationRejectionRequest;
import edu.vinu.domain.application.request.ApplicationSelectionRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InstituteTeacherService {
    ApplicationSelectionResponse onBoardTeachers(ApplicationSelectionRequest request);

    ApplicationRejectionResponse rejectApplications(ApplicationRejectionRequest request);

    Page<InstituteTeacherResponse> getAllTeachersByInstitute(int page, int size, String direction, String sortBy);

    InstituteTeacherStatsResponse getInstituteTeacherStats();

    List<TeacherBasicResponse> getAllTeachersByCurrentInstitute();

    TeacherEntity getCurrentInstituteRelatedTeacherEntityById(Long id);
}
