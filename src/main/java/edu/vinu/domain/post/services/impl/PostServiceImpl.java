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

package edu.vinu.domain.post.services.impl;

import edu.vinu.common.exception.custom.InternalServerErrorException;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.post.dto.request.PostCreateRequest;
import edu.vinu.domain.post.dto.request.PostMediaItemRequest;
import edu.vinu.domain.post.dto.request.PostUpdateRequest;
import edu.vinu.domain.post.dto.response.PostResponse;
import edu.vinu.domain.post.dto.response.UserPostResponse;
import edu.vinu.domain.post.entity.Post;
import edu.vinu.domain.post.entity.PostMedia;
import edu.vinu.domain.post.enums.PostStatus;
import edu.vinu.domain.post.mapper.PostMapper;
import edu.vinu.domain.post.repository.PostRepository;
import edu.vinu.domain.post.services.PostService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import edu.vinu.infastructure.service.file_storage.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FileService fileService;
    private final UserService userService;

    @Value("${file.post-path}")
    private String postPath;

    @Override
    public ApiResponse<PostResponse> createPost(PostCreateRequest request, List<MultipartFile> files) {

        UserEntity currentUser = userService.getCurrentUser();

        Post post = PostMapper.toPostEntity(request, currentUser);
        List<String> savedFileNames = new ArrayList<>();

        List<PostMediaItemRequest> mediaItems = request.mediaItems();

        if (files != null && !files.isEmpty()) {
            if (mediaItems == null || mediaItems.size() != files.size()) {
                throw new InvalidInputException("Media types must match the number of uploaded files.");
            }

            validateFileOrders(mediaItems, files.size());

            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file.isEmpty()) continue;

                String savedFileName = savePostFile(file);
                savedFileNames.add(savedFileName);

                PostMediaItemRequest mediaType = request.mediaItems().get(i);

                PostMedia media = PostMedia.builder()
                        .post(post)
                        .mediaUrl(savedFileName)
                        .mediaType(mediaType.mediaType())
                        .fileOrder(mediaType.fileOrder() != null ? mediaType.fileOrder() : i + 1)
                        .build();

                post.getMediaList().add(media);
            }
        }

        try {
            Post savedPost = postRepository.save(post);
            return createApiResponse("Post created successfully", PostMapper.toPostResponse(savedPost));
        } catch (Exception e) {
            savedFileNames.forEach(this::deletePostFile);
            throw new InternalServerErrorException("Failed to save post. Uploaded files reverted.");
        }
    }

    @Override
    public ApiResponse<PostResponse> publishDraftPost(Long postId) {
        UserEntity currentUser = userService.getCurrentUser();

        if (postRepository.isPostOwner(postId, currentUser.getId()) == 0) {
            throw new InvalidInputException("You are not authorized to publish this post.");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        if (post.getStatus() == PostStatus.PUBLISHED) {
            throw new InvalidInputException("Post is already published.");
        }

        post.setStatus(PostStatus.PUBLISHED);
        post.setPublishedDate(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);
        return createApiResponse("Post published successfully", PostMapper.toPostResponse(updatedPost));
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));
    }

    @Override
    @Transactional
    public ApiResponse<PostResponse> updatePost(Long postId, PostUpdateRequest request, List<MultipartFile> files) {
        UserEntity currentUser = userService.getCurrentUser();
        Post post = getPostById(postId);

        isOwner(post.getUser().getId(), currentUser.getId());

        if (request.caption() != null) {
            post.setCaption(request.caption());
        }

        post.setVisibility(request.visibility());

        if (request.status() != null) {
            PostStatus oldStatus = post.getStatus();
            post.setStatus(request.status());

            if (oldStatus != PostStatus.PUBLISHED && request.status() == PostStatus.PUBLISHED && post.getPublishedDate() == null) {
                post.setPublishedDate(LocalDateTime.now());
            }
        }

        List<String> savedFileNames = new ArrayList<>();
        List<String> filesToDelete = new ArrayList<>();

        // Register cleanup before performing file operations.
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    @Override
                    public void afterCommit() {
                        // Database committed successfully.
                        // Now it is safe to delete old physical files.
                        filesToDelete.forEach(PostServiceImpl.this::deletePostFile);
                    }

                    @Override
                    public void afterCompletion(int status) {
                        if (status == STATUS_ROLLED_BACK) {
                            // Database rolled back.
                            // Remove newly uploaded physical files.
                            savedFileNames.forEach(PostServiceImpl.this::deletePostFile);
                        }
                    }
                }
        );

        try {
            // 1. Identify and remove existing media.
            if (request.removeMediaIds() != null && !request.removeMediaIds().isEmpty()) {

                List<PostMedia> mediaToRemove = post.getMediaList().stream()
                        .filter(m -> request.removeMediaIds().contains(m.getId()))
                        .toList();

                for (PostMedia media : mediaToRemove) {
                    filesToDelete.add(media.getMediaUrl());
                    post.getMediaList().remove(media);
                }
            }

            // 2. Process new media.
            List<PostMediaItemRequest> newMediaItems = request.newMediaItems();

            if (files != null && !files.isEmpty()) {

                if (newMediaItems == null || newMediaItems.size() != files.size()) {
                    throw new InvalidInputException("New media types metadata must match the number of uploaded files.");
                }

                validateFileOrders(newMediaItems, post.getMediaList().size() + files.size());

                int currentMaxOrder = post.getMediaList().stream()
                        .mapToInt(PostMedia::getFileOrder)
                        .max()
                        .orElse(0);

                for (int i = 0; i < files.size(); i++) {

                    MultipartFile file = files.get(i);

                    if (file.isEmpty()) continue;

                    String savedFileName = savePostFile(file);
                    savedFileNames.add(savedFileName);

                    PostMediaItemRequest mediaMeta = newMediaItems.get(i);

                    PostMedia media = PostMedia.builder()
                            .post(post)
                            .mediaUrl(savedFileName)
                            .mediaType(mediaMeta.mediaType())
                            .fileOrder(
                                    mediaMeta.fileOrder() != null
                                            ? mediaMeta.fileOrder()
                                            : currentMaxOrder + i + 1
                            )
                            .build();

                    post.getMediaList().add(media);
                }
            }

            // 3. Persist changes.
            Post updatedPost = postRepository.save(post);

            return createApiResponse("Post updated successfully", PostMapper.toPostResponse(updatedPost));

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to update post.");
        }
    }

    private void validateFileOrders(List<PostMediaItemRequest> mediaItems, int totalFiles) {
        Set<Integer> orderSet = new HashSet<>();

        for (int i = 0; i < mediaItems.size(); i++) {
            PostMediaItemRequest item = mediaItems.get(i);
            int order = item.fileOrder() != null ? item.fileOrder() : (i + 1);

            if (order < 1 || order > totalFiles) {
                throw new InvalidInputException("fileOrder value (" + order + ") must be between 1 and " + totalFiles + ".");
            }

            if (!orderSet.add(order)) {
                throw new InvalidInputException("Duplicate fileOrder detected: " + order + ". Each media item must have a unique order.");
            }
        }
    }


    private String savePostFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = fileService.extractExtension(originalFileName);
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        String fileName = UUID.randomUUID() + extension;
        fileService.saveFile(file, getPostPath(), fileName, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private void deletePostFile(String fileName) {
        fileService.delete(Path.of(postPath, fileName));
    }

    private Path getPostPath() {
        return Path.of(postPath);
    }

    private void isOwner(Long postUserId, Long currentUserId){
        if (!postUserId.equals(currentUserId)) {
            throw new InvalidInputException("You are not authorized to update this post.");
        }
    }
}
