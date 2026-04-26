/*
 * Copyright (c) 2025 vinuth sri arampath
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

package edu.vinu.entity.user_entities;

import edu.vinu.entity.RoleEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_disabled = true WHERE id = ?")
@FilterDef(name = "softDeleteFilter", parameters = @ParamDef(name = "isDisabled",type = Boolean.class))
@Filter(name = "softDeleteFilter", condition = "is_disabled = :isDisabled")
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String contact;

    @NaturalId
    private String email;
    private String password;

    private boolean isDisabled=false;

    @Column(unique = true , nullable = false)
    private String userSlug;

    @Nullable
    private String dp;

    @Nullable
    private String banner;

    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false,updatable = false)
    private RoleEntity role;

    @OneToOne(mappedBy = "user")
    private StudentEntity student;

    @OneToOne(mappedBy = "user")
    private TeacherEntity teacher;

    @OneToOne(mappedBy = "user")
    private InstituteEntity institute;

    @CreationTimestamp
    @Column(name = "created_date",nullable = false,updatable = false)
    private LocalDateTime creationTimeStamp;

    @LastModifiedDate
    @Column(name = "last_modified_date",insertable = false)
    private LocalDateTime updatedAt;

}
