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

package edu.vinu.common.util;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import org.springframework.data.domain.Page;

public class ResponseUtil {
    public static <T> ApiResponse<T> createApiResponse(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T>PaginatedApiResponse<T> createPaginatedApiResponse(String message, Page<T> page){
        return new PaginatedApiResponse<>(message, page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages(), page.isLast());
    }
}
