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

package edu.vinu.domain.messaging.mapper;

import edu.vinu.domain.messaging.dtos.response.MessageAttachmentResponse;
import edu.vinu.domain.messaging.entity.MessageAttachment;

public class MessageAttachmentMapper {

    public static MessageAttachmentResponse toMessageAttachmentResponse(MessageAttachment attachment) {
        return MessageAttachmentResponse.builder()
                .id(attachment.getId())
                .fileUrl(attachment.getFileUrl())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .fileSize(attachment.getFileSize())
                .build();
    }
}
