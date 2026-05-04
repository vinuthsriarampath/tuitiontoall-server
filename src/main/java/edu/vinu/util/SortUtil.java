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

package edu.vinu.util;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortUtil {

    public static Sort buildSort(String direction, List<String> sortBy, List<String> defaultSortBy){

        List<String> effectedSortBy = (sortBy == null || sortBy.isEmpty()) ? defaultSortBy : sortBy;

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        List<Sort.Order> orders = new ArrayList<>();

        System.out.println("Received sort parameters: " + sortBy);
        System.out.println("Default sort parameters: " + defaultSortBy);
        System.out.println("Effective sort parameters: " + effectedSortBy);

        for (String param : effectedSortBy){


            if (param == null || param.isBlank()) continue;

            System.out.println("Processing sort parameter: " + param);
            orders.add(new Sort.Order(sortDirection, param));

        }

        return Sort.by(orders);
    }

}
