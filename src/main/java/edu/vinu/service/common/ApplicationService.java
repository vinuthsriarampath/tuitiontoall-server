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

import edu.vinu.model.Application;
import edu.vinu.response.ApplicationDetailsResponse;
import org.springframework.data.domain.Page;

public interface ApplicationService {
    Application createApplication(Long vacancyId);

    boolean isUserAlreadyApplied(Long userId,Long vacancyId);

    Page<ApplicationDetailsResponse> getApplicationsByVacancy(Long vacancyId, int page, int size, String direction, String sortBy);
}
