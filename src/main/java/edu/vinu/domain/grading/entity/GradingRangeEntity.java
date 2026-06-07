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

package edu.vinu.domain.grading.entity;

import edu.vinu.entity.AssignmentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "grading_range",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_assignment_grading_range",
                        columnNames = {"assignment_id","min_marks","max_marks"}
                ),
                @UniqueConstraint(
                        name = "uk_assignment_desired_grade",
                        columnNames = {"assignment_id","desired_grade"}
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradingRangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id",nullable = false)
    private AssignmentEntity assignment;

    @Column(name = "min_marks",nullable = false)
    private Integer minMarks;

    @Column(name = "max_marks",nullable = false)
    private Integer maxMarks;

    @Column(name = "desired_grade",nullable = false, length = 5)
    private String desiredGrade;

    @Column(name = "description")
    private String description;
}
