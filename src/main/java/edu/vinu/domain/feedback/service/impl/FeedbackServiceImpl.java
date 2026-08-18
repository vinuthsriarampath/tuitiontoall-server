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

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.exception.custom.BadRequestException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.common.util.SortUtil;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.feedback.entity.Feedback;
import edu.vinu.domain.feedback.enums.FeedbackEligibilityReason;
import edu.vinu.domain.feedback.mapper.FeedbackMapper;
import edu.vinu.domain.feedback.repository.FeedbackRepository;
import edu.vinu.domain.feedback.request.FeedbackCreateRequest;
import edu.vinu.domain.feedback.response.FeedbackEligibilityResponse;
import edu.vinu.domain.feedback.response.FeedbackResponse;
import edu.vinu.domain.feedback.service.FeedbackService;
import edu.vinu.domain.student_batch_enrollment.repository.StudentBatchEnrollmentRepository;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserAuthenticationService authService;
    private final CourseService courseService;
    private final UserService userService;
    private final StudentBatchEnrollmentRepository enrollmentRepository;

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

    @Override
    public PaginatedApiResponse<FeedbackResponse> getFeedbacksByCourse(Long courseId, PaginationRequest pagination) {
        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(), SortUtil.buildSort(pagination.direction(), pagination.sortBy(), List.of("created_date")));
        Page<FeedbackResponse> pageData = feedbackRepository.getFeedbacksByCourse(courseId,pageable).map(FeedbackMapper::toFeedbackResponse);

        return PaginatedApiResponse.<FeedbackResponse>builder()
                .message("Feedbacks fetched successfully")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .last(pageData.isLast())
                .build();
    }

    @Override
    public FeedbackEligibilityResponse checkFeedbackEligibility(Long courseId) {
        UserEntity userEntity = userService.getUserEntityByEmail(authService.getCurrentUserEmail());
        CourseEntity courseEntity;
        try {
            courseEntity = courseService.getCourseEntityById(courseId);
        } catch (Exception e) {
            return FeedbackEligibilityResponse.builder()
                    .canFeedback(false)
                    .reason(FeedbackEligibilityReason.COURSE_NOT_FOUND)
                    .build();
        }

        if(!userEntity.getRole().getRole().equals("student")){
            return FeedbackEligibilityResponse.builder()
                    .canFeedback(false)
                    .reason(FeedbackEligibilityReason.NOT_STUDENT)
                    .build();

        } else if (enrollmentRepository.existsEnrollmentByStudentAndCourse(userEntity.getStudent().getId(), courseEntity.getId()) == 0) {
            return FeedbackEligibilityResponse.builder()
                    .canFeedback(false)
                    .reason(FeedbackEligibilityReason.NOT_ENROLLED)
                    .build();

        }

        if(feedbackRepository.countUserFeedbacksByCourseId(courseEntity.getId(), userEntity.getId()) > 0) {
            return FeedbackEligibilityResponse.builder()
                    .canFeedback(false)
                    .reason(FeedbackEligibilityReason.ALREADY_SUBMITTED)
                    .build();
        }

        return FeedbackEligibilityResponse.builder()
                .canFeedback(true)
                .reason(FeedbackEligibilityReason.ELIGIBLE)
                .build();
    }
}
