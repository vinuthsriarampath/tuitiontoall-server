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

package edu.vinu.common.dto;

import java.util.List;

public record PaginationRequest(
        Integer  page,
        Integer size,
        String direction,
        List<String> sortBy
) {
    public PaginationRequest {
        page = page != null ? page : 0;
        size = size != null ? size : 10;
        direction = direction != null ? direction : "desc";
        sortBy = sortBy != null && !sortBy.isEmpty()
                ? sortBy
                : List.of("created_date");
    }
}
