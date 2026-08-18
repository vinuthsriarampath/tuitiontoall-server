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

package edu.vinu.domain.feedback.service.impl;

import edu.vinu.common.exception.custom.BadRequestException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.feedback.entity.Feedback;
import edu.vinu.domain.feedback.mapper.FeedbackMapper;
import edu.vinu.domain.feedback.repository.FeedbackRepository;
import edu.vinu.domain.feedback.request.FeedbackCreateRequest;
import edu.vinu.domain.feedback.service.FeedbackService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserAuthenticationService authService;
    private final CourseService courseService;
    private final UserService userService;

    @Override
    public ApiResponse submitFeedback(FeedbackCreateRequest request) {
        UserEntity userEntity = userService.getUserEntityByEmail(authService.getCurrentUserEmail());
        CourseEntity courseEntity = courseService.getCourseEntityById(request.courseId());

        if(feedbackRepository.countUserFeedbacksByCourseId(courseEntity.getId(), userEntity.getId()) > 0) {
            throw new BadRequestException("User has already given a feedback for this course");
        }

        Feedback feedbackEntity = Feedback.builder()
                .feedback(request.feedback())
                .user(userEntity)
                .course(courseEntity)
                .build();

        Feedback save = feedbackRepository.save(feedbackEntity);
        return ApiResponse.builder()
                .message("Feedback submitted successfully")
                .data(FeedbackMapper.toFeedbackResponse(save))
                .build();
    }
}
