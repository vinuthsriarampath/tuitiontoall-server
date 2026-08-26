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

package edu.vinu.domain.resource.mapper;

import edu.vinu.domain.resource.entity.ResourceEntity;
import edu.vinu.domain.resource.response.ResourceResponse;

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
