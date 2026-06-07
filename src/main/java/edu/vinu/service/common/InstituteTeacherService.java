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

import edu.vinu.domain.user.entity.TeacherEntity;
import edu.vinu.request.ApplicationRejectionRequest;
import edu.vinu.request.ApplicationSelectionRequest;
import edu.vinu.response.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InstituteTeacherService {
    ApplicationSelectionResponse onBoardTeachers(ApplicationSelectionRequest request);

    ApplicationRejectionResponse rejectApplications(ApplicationRejectionRequest request);

    Page<InstituteTeacherResponse> getAllTeachersByInstitute(int page,int size, String direction, String sortBy);

    InstituteTeacherStatsResponse getInstituteTeacherStats();

    List<TeacherBasicResponse> getAllTeachersByCurrentInstitute();

    TeacherEntity getCurrentInstituteRelatedTeacherEntityById(Long id);
}
