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

package edu.vinu.domain.student_assignment_submit.entity;

import edu.vinu.domain.assignment.entity.AssignmentEntity;
import edu.vinu.domain.student.entity.StudentEntity;
import edu.vinu.domain.student_assignment_submit.enums.AssignmentSubmitStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "student_assignment_submit",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"assignment_id","student_id","attempt_no"}
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAssignmentSubmit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "url",nullable = false)
    private String url;

    @Column(name = "grade", length = 5)
    private String grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private AssignmentSubmitStatus status;

    @Builder.Default
    @Column(name = "marks_gained",nullable = false)
    private int marksGained = 0;

    @Builder.Default
    @Column(name = "attempt_no",nullable = false)
    private int attemptNo = 1;

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false, nullable = false)
    private LocalDateTime submittedAt;

    @UpdateTimestamp
    @Column(name = "last_modified_date", insertable = false)
    private LocalDateTime lastModifiedDate;

    @ManyToOne(optional = false, fetch =  FetchType.LAZY)
    @JoinColumn(name = "student_id",nullable = false)
    private StudentEntity student;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id",nullable = false)
    private AssignmentEntity assignment;
}
