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

import edu.vinu.domain.post.dto.response.PostMediaResponse;
import edu.vinu.domain.post.mapper.PostMediaMapper;
import edu.vinu.domain.post.repository.PostMediaRepository;
import edu.vinu.domain.post.services.PostMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostMediaServiceImpl implements PostMediaService {

    private final PostMediaRepository postMediaRepository;


    @Override
    public List<PostMediaResponse> getPostMediaByPostId(Long postId) {
        return postMediaRepository.findAllByPostId(postId).stream()
                .map(PostMediaMapper::toPostMediaResponse)
                .toList();
    }
}
