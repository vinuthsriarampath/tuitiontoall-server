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

import edu.vinu.request.ApplicationRejectionRequest;
import edu.vinu.request.ApplicationSelectionRequest;
import edu.vinu.response.ApplicationRejectionResponse;
import edu.vinu.response.ApplicationSelectionResponse;
import jakarta.validation.Valid;

public interface InstituteTeacherService {
    ApplicationSelectionResponse onBoardTeachers(ApplicationSelectionRequest request);

    ApplicationRejectionResponse rejectApplications(@Valid ApplicationRejectionRequest request);
}
