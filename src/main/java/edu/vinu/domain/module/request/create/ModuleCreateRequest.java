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

package edu.vinu.domain.module.request.create;

import edu.vinu.domain.module.request.create.enums.ModuleCreateStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleCreateRequest {
    @NotBlank(message = "Module name is mandatory!")
    private String name;
    @NotNull(message = "Module status is mandatory!")
    private ModuleCreateStatus status;
    @NotNull(message = "Batch ID is mandatory!")
    private Long batchId;
    @NotNull(message = "Teacher ID is mandatory!")
    private Long teacherId;
}
