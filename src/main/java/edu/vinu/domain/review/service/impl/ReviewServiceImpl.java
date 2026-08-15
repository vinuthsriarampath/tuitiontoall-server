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

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.course.repository.CourseRepository;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.review.entity.Review;
import edu.vinu.domain.review.repository.ReviewRepository;
import edu.vinu.domain.review.request.ReviewCreateRequest;
import edu.vinu.domain.review.service.ReviewService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
}
