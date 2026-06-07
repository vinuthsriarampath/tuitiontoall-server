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

package edu.vinu.seeder;

import com.github.javafaker.Faker;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.user.entity.RoleEntity;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.enums.*;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.UserAlreadyExistException;
import edu.vinu.domain.institute.dto.Institute;
import edu.vinu.repository.CourseRepository;
import edu.vinu.domain.user.repository.RoleRepository;
import edu.vinu.domain.user.repository.UserRepository;
import edu.vinu.domain.user.request.registration.InstituteRegistrationRequest;
import edu.vinu.domain.user.request.registration.StudentRegistrationRequest;
import edu.vinu.domain.user.request.registration.TeacherRegistrationRequest;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final Faker faker;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserAuthenticationService authService;
    private final CourseRepository courseRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;

    @Override
    public void run(String... args) {
        seedRoles();

        if (userRepository.count() == 0){
            seedInstitutes();
            seedTeachers();
            seedStudents();
        }else {
            log.info("User Table already contains data, Skipping seeding. ⏏️");
        }

        if(courseRepository.count() == 0){
            seedCourses();
        }else {
            log.info("Course Table already contains data, Skipping seeding. ⏏️");
        }
    }

    private void seedInstitutes(){
        InstituteRegistrationRequest request = new InstituteRegistrationRequest();
        for (int i = 0; i < 50; i++) {
            try {
                request.setInstituteName(faker.university().name());
                request.setPassword("Password@123");
                request.setAddress(faker.address().fullAddress());
                request.setEmail("institute"+(i+1)+"@exm.com");
                request.setContact(faker.number().digits(10));
                authService.registerInstitute(request);
            } catch (UserAlreadyExistException | InvalidInputException ex) {
                i--;
            }
        }
        log.info("Seeded 50 institutes 🏫");
    }

    private void seedTeachers(){
        TeacherRegistrationRequest request = new TeacherRegistrationRequest();
        for (int i = 0; i < 50; i++) {
            try {
                request.setFirstName(faker.name().firstName());
                request.setLastName(faker.name().lastName());
                request.setDob(faker.date().birthday(6, 100).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                request.setContact(faker.number().digits(10));
                request.setAddress(faker.address().fullAddress());
                request.setEmail("teacher"+(i+1)+"@exm.com");
                request.setPassword("Password@123");
                authService.registerTeacher(request);
            } catch (UserAlreadyExistException | InvalidInputException ex) {
                i--;
            }
        }
        log.info("Seeded 50 Teachers 🧑‍🏫");
    }

    private void seedStudents(){
        StudentRegistrationRequest request = new StudentRegistrationRequest();
        for (int i = 0; i < 50; i++) {
            try {
                request.setFirstName(faker.name().firstName());
                request.setLastName(faker.name().lastName());
                request.setDob(faker.date().birthday(6, 100).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                request.setContact(faker.number().digits(10));
                request.setAddress(faker.address().fullAddress());
                request.setEmail("student"+(i+1)+"@exm.com");
                request.setPassword("Password@123");
                authService.registerStudent(request);
            } catch (UserAlreadyExistException | InvalidInputException ex) {
                i--;
            }
        }
        log.info("Seeded 50 Student 🧑‍🎓");
    }

    private void seedCourses(){
        List<Institute> institutes = userService.getAllInstitutes();
        if (institutes.isEmpty()) return;

        CourseEntity request = new CourseEntity();

        for (int i = 0;i < 100; i++){
            try {
                Institute institute = institutes.get(faker.random().nextInt(institutes.size()));
                request.setId(null);
                request.setTitle(faker.educator().course());
                request.setDescription(faker.lorem().sentence());
                request.setDurationInHours(faker.number().numberBetween(10, 100));
                request.setPrice(faker.number().randomDouble(2, 100, 1000));

                request.setLevel(CourseLevel.values()[faker.random().nextInt(CourseLevel.values().length)]);
                request.setCategory(CourseCategory.values()[faker.random().nextInt(CourseCategory.values().length)]);
                request.setStatus(CourseStatus.values()[faker.random().nextInt(CourseStatus.values().length)]);
                request.setLanguage(CourseLanguage.values()[faker.random().nextInt(CourseLanguage.values().length)]);
                request.setMode(CourseMode.values()[faker.random().nextInt(CourseMode.values().length)]);
                request.setAvg_rating(BigDecimal.valueOf(0.0));
                request.setTotal_no_ratings(0);

                request.setInstitute(mapper.map(institute, InstituteEntity.class));

                courseRepository.save(request);
            }catch (RuntimeException ex){
                log.error("Error seeding course data: {}", ex.getMessage());
            }
        }
        log.info("Seeded 100 Courses 📚");
    }

    private void seedRoles(){
        List<String> roles = new ArrayList<>();
        roles.add("institute");
        roles.add("teacher");
        roles.add("student");

        for (String role : roles){
            if(roleRepository.existsByRole(role)) continue;
            RoleEntity newRole = new RoleEntity();
            newRole.setRole(role);
            roleRepository.save(newRole);
        }
        log.info("Roles Seeded successfully 🥷");
    }
}
