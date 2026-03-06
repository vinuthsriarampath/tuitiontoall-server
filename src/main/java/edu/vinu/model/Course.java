/*
 * Copyright (c) 2025 vinuth sri arampath
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

package edu.vinu.model;

import edu.vinu.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Long id;
    private String title;
    private String description;
    private int durationInHours;
    private Double price;
    private CourseLevel level;
    private CourseCategory category;
    private CourseStatus status;
    private CourseLanguage language;
    private CourseMode mode;
    private String thumbnail;
    private BigDecimal avg_rating= BigDecimal.ZERO;
    private Integer total_no_ratings=0;
    private LocalDateTime creationTimeStamp;
    private LocalDateTime updatedAt;
}
