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

import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.domain.assignment.repository.AssignmentRepository;
import edu.vinu.domain.assignment.service.AssignmentSecurityService;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentSecurityServiceImpl implements AssignmentSecurityService {

    private final UserAuthenticationService authService;
    private final UserService userService;
    private final AssignmentRepository assignmentRepository;

    @Override
    public void validateAssignmentAccess(Long assignmentId) {

        if (!assignmentRepository.existsById(assignmentId)) {
            throw new NotFoundException("Assignment not found with id: " + assignmentId);
        }

        UserEntity currentUser = userService.getUserEntityByEmail(authService.getCurrentUserEmail());
        String userRole = currentUser.getRole().getRole();

        boolean hasAccess = switch (userRole.toLowerCase()){
            case "institute"->{
                if (currentUser.getInstitute() == null) throw new UnauthorizedException("User is not associated with any institute");
                yield assignmentRepository.instituteAccess(assignmentId, currentUser.getInstitute().getId()) > 0;
            }
            case "teacher"->{
                if (currentUser.getTeacher() == null) throw new UnauthorizedException("User is not associated with any teacher");
                yield assignmentRepository.teacherAccess(assignmentId, currentUser.getTeacher().getId()) > 0;
            }
            case "student"->{
                if (currentUser.getStudent() == null) throw new UnauthorizedException("User is not associated with any student");
                yield assignmentRepository.studentAccess(assignmentId, currentUser.getStudent().getId()) > 0;
            }
            default -> false;
        };

        if (!hasAccess){
            throw new UnauthorizedException("User is not authorized to access this assignment");
        }
    }
}
