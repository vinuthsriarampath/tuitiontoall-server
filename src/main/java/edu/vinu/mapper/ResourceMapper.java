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

package edu.vinu.mapper;

import edu.vinu.entity.ResourceEntity;
import edu.vinu.response.resource.ResourceResponse;

public class ResourceMapper {

    public static ResourceResponse toResourceResponse(ResourceEntity entity){
        return ResourceResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .chapterId(entity.getChapter().getId())
                .fileName(entity.getFileName())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }
}
