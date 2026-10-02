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

package edu.vinu.domain.teacher.dtos.response;

import edu.vinu.domain.course.response.TeacherBasicCourseResponse;
import edu.vinu.domain.schedule_lecture.response.ScheduleLectureResponse;
import edu.vinu.domain.student_assignment_submit.response.NonGradedSubmissionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherBootstrapResponse{
        TeacherDashboardStats stats;
        @Builder.Default
        List<TeacherBasicCourseResponse> assignedPublishedCourses = new ArrayList<>();
        @Builder.Default
        List<ScheduleLectureResponse> upcomingLectureSchedules = new ArrayList<>();
        @Builder.Default
        List<NonGradedSubmissionResponse> nonGradedSubmissions = new ArrayList<>();
}
