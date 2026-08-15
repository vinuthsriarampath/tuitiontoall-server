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

package edu.vinu.domain.review.service.impl;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.exception.custom.BadRequestException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.common.util.SortUtil;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.course.repository.CourseRepository;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.review.entity.Review;
import edu.vinu.domain.review.mapper.ReviewMapper;
import edu.vinu.domain.review.repository.ReviewRepository;
import edu.vinu.domain.review.repository.projector.BasicReviewProjector;
import edu.vinu.domain.review.request.ReviewCreateRequest;
import edu.vinu.domain.review.response.BasicReviewResponse;
import edu.vinu.domain.review.service.ReviewService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final UserAuthenticationService authService;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public ApiResponse createReview(ReviewCreateRequest request) {
        UserEntity userEntity = userService.getUserEntityByEmail(authService.getCurrentUserEmail());
        CourseEntity courseEntity = courseService.getCourseEntityById(request.courseId());

        if(reviewRepository.countUserReviewsByCourseId(courseEntity.getId(), userEntity.getId()) > 0){
            throw new BadRequestException("User has already reviewed this course");
        }

        Review review = Review.builder()
                .review(request.review())
                .rating(request.rating())
                .user(userEntity)
                .course(courseEntity)
                .build();
        reviewRepository.save(review);

        int oldTotal = courseEntity.getTotal_no_ratings();
        BigDecimal oldAverage = courseEntity.getAvg_rating();

        int newTotal = oldTotal + 1;

        BigDecimal newAverage = oldAverage
                .multiply(BigDecimal.valueOf(oldTotal))
                .add(BigDecimal.valueOf(request.rating()))
                .divide(
                        BigDecimal.valueOf(newTotal),
                        2,
                        RoundingMode.HALF_UP
                );

        courseEntity.setTotal_no_ratings(newTotal);
        courseEntity.setAvg_rating(newAverage);

        courseRepository.save(courseEntity);

        return ApiResponse.builder()
                .message("Review created successfully")
                .data(null)
                .build();
    }

    @Override
    public PaginatedApiResponse<BasicReviewResponse> getReviewsByCourseId(Long courseId, PaginationRequest pagination) {
        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(), SortUtil.buildSort(pagination.direction(), pagination.sortBy(), List.of(("created_date"))));
        Page<BasicReviewResponse> pageData = reviewRepository.findReviewsByCourseId(courseId, pageable).map(ReviewMapper::toBasicReviewResponse);
        return PaginatedApiResponse.<BasicReviewResponse>builder()
                .message("Reviews fetched successfully!")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .last(pageData.isLast())
                .build();
    }
}
