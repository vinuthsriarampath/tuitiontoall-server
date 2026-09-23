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

package edu.vinu.domain.assignment.repository;

import edu.vinu.domain.assignment.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity,Long> {

    @Query(value = """
        SELECT EXISTS(
        SELECT 1
        FROM assignment a
        WHERE a.id = :assignmentId
            AND (
                    (a.type = 'MODULE' AND EXISTS(
                        SELECT 1
                        FROM module_assignment ma
                        INNER JOIN module m ON m.id = ma.module_id
                        INNER JOIN batch b ON m.batch_id = b.id
                        INNER JOIN courses cr ON b.course_id = cr.id
                        WHERE cr.institute_id = :instituteId
                    ))
                    OR (a.type = 'CHAPTER' AND EXISTS(
                        SELECT 1
                        FROM chapter_assignment ca
                        INNER JOIN chapter c ON c.id = ca.chapter_id
                        INNER JOIN module m ON c.module_id = m.id
                        INNER JOIN batch b ON m.batch_id = b.id
                        INNER JOIN courses cr ON b.course_id = cr.id
                        WHERE cr.institute_id = :instituteId
                    ))
            )
        )
    """, nativeQuery = true)
    int instituteAccess(Long assignmentId, Long instituteId);

    @Query(value = """
    SELECT EXISTS(
            SELECT 1
            FROM assignment a
            WHERE
                a.id = :assignmentId
                AND (
                        (a.type = 'MODULE' AND EXISTS(
                            SELECT 1
                            FROM module_assignment ma
                            INNER JOIN module m ON m.id = ma.module_id
                            INNER JOIN teacher t ON t.id = m.teacher_id
                            WHERE t.id = :teacherId
                        ))
                        OR (a.type = 'CHAPTER' AND EXISTS(
                            SELECT 1
                            FROM chapter_assignment ca
                            INNER JOIN chapter c ON c.id = ca.chapter_id
                            INNER JOIN module m ON c.module_id = m.id
                            INNER JOIN teacher t ON t.id = m.teacher_id
                            WHERE t.id = :teacherId
                        ))
                    )
        )
    """,nativeQuery = true)
    int teacherAccess(Long assignmentId, Long teacherId);

    @Query(value = """
    SELECT EXISTS(
        SELECT 1
        FROM assignment a
        WHERE
            a.id = :assignmentId
            AND (
                (a.type = 'MODULE' AND EXISTS(
                    SELECT 1
                    FROM module_assignment ma
                    INNER JOIN module m ON m.id = ma.module_id = m.id
                    INNER JOIN student_batch_enrollment sbe ON sbe.batch_id = m.batch_id
                    WHERE sbe.student_id = :studentId
                ))
                OR (a.type = 'CHAPTER' AND EXISTS(
                    SELECT 1
                    FROM chapter_assignment ca
                    INNER JOIN chapter c ON c.id = ca.chapter_id
                    INNER JOIN module m ON c.module_id = m.id
                    INNER JOIN student_batch_enrollment sbe ON sbe.batch_id = m.batch_id
                    WHERE sbe.student_id = :studentId
                ))
            )
    )
    """,nativeQuery = true)
    int studentAccess(Long assignmentId, Long studentId);

    @Query(value = """
    SELECT COUNT(DISTINCT a.id)
    FROM assignment a
    INNER JOIN (
        SELECT ma.assignment_id, m.batch_id
        FROM module_assignment ma
        INNER JOIN module m ON ma.module_id = m.id
        INNER JOIN batch b ON m.batch_id = b.id
        WHERE b.batch_status <> 'COMPLETED'

        UNION ALL
    
        SELECT ca.assignment_id, m.batch_id
        FROM chapter_assignment ca
        INNER JOIN chapter c ON ca.chapter_id = c.id
        INNER JOIN module m ON c.module_id = m.id
        INNER JOIN batch b ON m.batch_id = b.id
        WHERE b.batch_status <> 'COMPLETED'
    
    ) AS target_assignments on target_assignments.assignment_id = a.id

    INNER JOIN student_batch_enrollment sbe ON target_assignments.batch_id = sbe.batch_id
    
    WHERE sbe.student_id = :studentId
      AND NOT EXISTS (
          SELECT 1
          FROM student_assignment_submit sas\s
          WHERE sas.assignment_id = a.id
            AND sas.student_id = :studentId
      );
    """,nativeQuery = true)
    Long countStudentsPendingAssignments(Long studentId);
}
