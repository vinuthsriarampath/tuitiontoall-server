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

import edu.vinu.entity.CourseEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("INSTITUTE")
@SQLDelete(sql = "UPDATE users SET is_disabled = true WHERE id = ?")
@Filter(name = "softDeleteFilter", condition = "is_disabled = :isDisabled")
public class InstituteEntity extends UserEntity {
    private String instituteName;
    @OneToMany(mappedBy = "institute")
    private List<CourseEntity> courses = new ArrayList<>();
}
