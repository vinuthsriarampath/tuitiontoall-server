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

package edu.vinu.domain.user_follow.entity;

import edu.vinu.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_follow",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"following_id", "follower_id"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "follower_id",nullable = false)
    UserEntity follower;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "following_id",nullable = false)
    UserEntity followingUser;

    @Builder.Default
    @Column(name = "followed_on", nullable = false)
    LocalDateTime followedOn = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_modified_date", insertable = false)
    LocalDateTime lastModifiedDate;

}
