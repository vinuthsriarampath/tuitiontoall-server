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

package edu.vinu.domain.messaging.services.impl;

import edu.vinu.common.exception.custom.BadRequestException;
import edu.vinu.domain.messaging.dtos.response.TempMessageAttachmentResponse;
import edu.vinu.domain.messaging.enums.MessageAttachmentType;
import edu.vinu.domain.messaging.services.ChatAttachmentService;
import edu.vinu.infastructure.service.file_storage.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatAttachmentServiceImpl implements ChatAttachmentService {

    private final FileService fileService;

    @Value("${file.message-attachment.temp-path}")
    private String tempAttachmentPath;

    @Value("${file.message-attachment.attachment-path}")
    private String attachmentPath;

    @Override
    public List<TempMessageAttachmentResponse> tempStoreFiles(List<MultipartFile> files) {
        if (files.isEmpty() || files.stream().allMatch(MultipartFile::isEmpty)) {
            throw new BadRequestException("Files are required for uploading");
        }

        List<String> successfullyUploadedFiles = new ArrayList<>();

        try {
            List<TempMessageAttachmentResponse> responses = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    throw new BadRequestException("File " + file.getOriginalFilename() + " is empty");
                }

                String originalFileName = file.getOriginalFilename();
                String fileType = file.getContentType();
                long fileSize = file.getSize();

                if(originalFileName == null || originalFileName.isEmpty()) {
                    throw new BadRequestException("Original file name is required");
                }
                String extension = fileService.extractExtension(originalFileName);
                String fileSavingName = UUID.randomUUID() + extension;

                String updatedOriginalFileName = originalFileName.replace(" ","_");


                // Save the file to the temporary path
                fileService.saveFile(file, getTempAttachmentPath(),fileSavingName, StandardCopyOption.REPLACE_EXISTING);

                responses.add(
                        TempMessageAttachmentResponse.builder()
                                .fileUrl(fileSavingName)
                                .fileName(updatedOriginalFileName)
                                .fileType(getAttachmentType(fileType))
                                .fileSize(fileSize)
                                .build()
                );

                successfullyUploadedFiles.add(fileSavingName);
            }

            return responses;
        } catch (BadRequestException e) {
            successfullyUploadedFiles.forEach(this::deleteTempAttachments);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void moveToPermanentStorage(String tempFileName) {
        Path sourcePath = getTempAttachmentPath().resolve(tempFileName);
        Path targetPath = getAttachmentPath().resolve(tempFileName);

        fileService.moveFile(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void moveBackToTempStorage(String fileName) {
        Path sourcePath = getAttachmentPath().resolve(fileName);
        Path targetPath = getTempAttachmentPath().resolve(fileName);

        fileService.moveFile(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private MessageAttachmentType getAttachmentType(String contentType) {
        if (contentType == null) {
            throw new BadRequestException("Content type is required to determine attachment type");
        }

        if (contentType.startsWith("image/")) {
            return MessageAttachmentType.IMAGE;
        } else if (contentType.startsWith("video/")) {
            return MessageAttachmentType.VIDEO;
        } else {
            return MessageAttachmentType.DOCUMENT;
        }
    }

    private void deleteTempAttachments(String fileName){
        fileService.delete(Path.of(tempAttachmentPath,fileName));
    }

    private Path getTempAttachmentPath(){
        return Path.of(tempAttachmentPath);
    }

    private Path getAttachmentPath(){
        return Path.of(attachmentPath);
    }
}
