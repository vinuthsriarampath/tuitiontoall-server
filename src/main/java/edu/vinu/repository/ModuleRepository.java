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

package edu.vinu.repository;

import edu.vinu.entity.ModuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {
    boolean existsByNameAndBatchId(String name, Long batchId);

    @Query(
            value = """
                    SELECT *
                    FROM module m
                    WHERE(:status IS NULL OR m.status = :status)
                    AND (:batchId IS NULL OR m.batch_id = :batchId)
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM module m
                    WHERE(:status IS NULL OR m.status = :status)
                    AND (:batchId IS NULL OR m.batch_id = :batchId)
            """,
            nativeQuery = true
    )
    Page<ModuleEntity> getAllModules(Pageable pageable, @Param("status") String status, @Param("batchId") Long batchId);
}
