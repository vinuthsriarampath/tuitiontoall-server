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

package edu.vinu.domain.assignment.service.impl;

import edu.vinu.domain.assignment.repository.AssignmentRepository;
import edu.vinu.domain.assignment.service.AssignmentStatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AssignmentStatServiceImpl implements AssignmentStatService {

    private final AssignmentRepository assignmentRepository;

    @Override
    public Long getStudentPendingAssignmentCount(Long studentId) {
        return assignmentRepository.countStudentsPendingAssignments(studentId);
    }

    @Override
    public Long getStudentCompletedAssignmentCount(Long studentId) {
        return assignmentRepository.countStudentsCompletedAssignments(studentId);
    }
}
