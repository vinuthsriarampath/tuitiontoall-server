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

package edu.vinu.entity;

import edu.vinu.enums.BatchEnrollmentStatus;
import edu.vinu.enums.BatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "batch",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_course_batch_name", columnNames = {"course_id", "name"})
        }
)
public class BatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    private CourseEntity course;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean is_seat_limited;

    @Column(nullable = false)
    private Integer max_seat_limit;

    @Column(nullable = false)
    private LocalDate start_date;

    @Column(nullable = false)
    private LocalTime start_time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchStatus batch_status=BatchStatus.PREPARATION;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchEnrollmentStatus enrollment_status=BatchEnrollmentStatus.OPEN;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime created_date;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime last_modified_date;
}
