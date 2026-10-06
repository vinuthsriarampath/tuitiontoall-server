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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FileService fileService;
    private final UserService userService;

    @Value("${file.post-path}")
    private String postPath;

    @Override
    public ApiResponse createPost(PostCreateRequest request, List<MultipartFile> files) {

        UserEntity currentUser = userService.getCurrentUser();

        Post post = PostMapper.toPostEntity(request, currentUser);
        List<String> savedFileNames = new ArrayList<>();

        List<PostMediaItemRequest> mediaItems = request.mediaItems();

        if(files != null && !files.isEmpty() ){
            if(mediaItems == null || mediaItems.size() != files.size()){
                throw new InvalidInputException("Media types must match the number of uploaded files.");
            }

            validateFileOrders(mediaItems, files.size());

            for(int i=0; i<files.size(); i++){
                MultipartFile file = files.get(i);
                if(file.isEmpty()) continue;

                String savedFileName = savePostFile(file);
                savedFileNames.add(savedFileName);

                PostMediaItemRequest mediaType = request.mediaItems().get(i);

                PostMedia media = PostMedia.builder()
                        .post(post)
                        .mediaUrl(savedFileName)
                        .mediaType(mediaType.mediaType())
                        .fileOrder(mediaType.fileOrder() != null ? mediaType.fileOrder() : i+1)
                        .build();

                post.getMediaList().add(media);
            }
        }

        try {
            Post savedPost = postRepository.save(post);
            return ApiResponse.builder()
                    .message("Post created successfully")
                    .data(PostMapper.toPostResponse(savedPost))
                    .build();
        } catch (Exception e) {
            savedFileNames.forEach(this::deletePostFile);
            throw new InternalServerErrorException("Failed to save post. Uploaded files reverted.");
        }
    }

    @Override
    public ApiResponse publishDraftPost(Long postId) {
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
        return ApiResponse.builder()
                .message("Post published successfully")
                .data(PostMapper.toPostResponse(updatedPost))
                .build();
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


    private String savePostFile(MultipartFile file){
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
}
