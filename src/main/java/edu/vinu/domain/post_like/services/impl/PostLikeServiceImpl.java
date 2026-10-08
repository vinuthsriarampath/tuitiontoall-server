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

package edu.vinu.domain.post_like.services.impl;

import edu.vinu.common.exception.custom.BadRequestException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.post.repository.PostRepository;
import edu.vinu.domain.post_like.entity.PostLike;
import edu.vinu.domain.post_like.repository.PostLikeRepository;
import edu.vinu.domain.post_like.services.PostLikeService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public void likePost(Long postId) {
        UserEntity currentUser = userService.getCurrentUser();

        if(likeRepository.isLikedByUser(postId, currentUser.getId()) > 0) {
            throw new BadRequestException("Post already liked by user!");
        }

        PostLike build = PostLike.builder()
                .post(postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found by id!")))
                .user(currentUser)
                .build();

        likeRepository.save(build);
    }
}
