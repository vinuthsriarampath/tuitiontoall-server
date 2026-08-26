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

import edu.vinu.domain.institute.dto.Institute;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.institute.request.InstituteDetailsUpdateRequest;
import edu.vinu.domain.user.dto.User;

import java.util.List;

public interface InstituteService {
    InstituteEntity getCurrentInstitute();

    List<Institute> getAllInstitutes();

    List<User> getAllInstitutesByName(String instituteName);

    Institute updateInstituteDetails(String currentEmail, InstituteDetailsUpdateRequest instituteDetailsUpdateRequest);
}
