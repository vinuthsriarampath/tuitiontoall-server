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

package edu.vinu.domain.user.helper;

import edu.vinu.domain.user.entity.UserEntity;

public class UserHelper {
    public static String getUserDisplayName(UserEntity user){
        String senderName;
        String senderRole = user.getRole().getRole();
        switch (senderRole){
            case "student"-> senderName = user.getStudent().getFirstName()+" "+ user.getStudent().getLastName();
            case "teacher"-> senderName = user.getTeacher().getFirstName()+" "+ user.getTeacher().getLastName();
            case "institute"-> senderName = user.getInstitute().getInstituteName();
            default -> senderName = user.getUserSlug();
        }
        return senderName;
    }
}
