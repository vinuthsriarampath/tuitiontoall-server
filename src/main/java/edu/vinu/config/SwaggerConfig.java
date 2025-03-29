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

package edu.vinu.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customApiConfig(){
        return new OpenAPI()
                .info(new Info()
                        .title("TuitionToAll API")
                        .version("2.0.0")
                        .description("API documentation for TuitionToAll v2.0.0")
                        .license(new License()
                                .name("MIT License")
                                .url("https://github.com/vinuthsriarampath/tuitiontoall-server/blob/d90c20e4307830f20783f6cf9c9c60f48e8c1084/LICENSE"))
                        .contact(new Contact()
                                .name("Vinuth Sri Arampath")
                                .email("vinuthsriarampath@outlook.com")
                                .url("https://github.com/vinuthsriarampath"))
                );

    }
}
