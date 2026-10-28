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

package edu.vinu.domain.module.mapper;

import edu.vinu.domain.module.enums.ModuleStatus;
import edu.vinu.domain.module.request.create.enums.ModuleCreateStatus;

public class ModuleStatusMapper {

    public static ModuleStatus toModuleStatus(ModuleCreateStatus status) {
        switch (status) {
            case DRAFT -> {
                return ModuleStatus.DRAFT;
            }
            case PUBLISHED -> {
                return ModuleStatus.PUBLISHED;
            }
            case LOCKED -> {
                return ModuleStatus.LOCKED;
            }
            default -> throw new IllegalArgumentException("Invalid module status: " + status);
        }
    }
}
