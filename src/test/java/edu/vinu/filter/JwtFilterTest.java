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

package edu.vinu.filter;

import edu.vinu.service.auth.impl.JwtService;
import edu.vinu.service.auth.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private ApplicationContext context;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtFilter jwtFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_authEndpoint_bypassesFilter() throws IOException, ServletException {
        request.setServletPath("/api/v2/auth/login");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, context, handlerExceptionResolver);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_authEndpoint_handlesIOException() throws IOException, ServletException {
        request.setServletPath("/api/v2/auth/login");
        IOException exception = new IOException("IO error");
        doThrow(exception).when(filterChain).doFilter(request, response);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(handlerExceptionResolver).resolveException(request, response, null, exception);
        verifyNoInteractions(jwtService, context);
    }

    @Test
    void doFilterInternal_authEndpoint_handlesServletException() throws IOException, ServletException {
        request.setServletPath("/api/v2/auth/login");
        ServletException exception = new ServletException("Servlet error");
        doThrow(exception).when(filterChain).doFilter(request, response);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(handlerExceptionResolver).resolveException(request, response, null, exception);
        verifyNoInteractions(jwtService, context);
    }

    @Test
    void doFilterInternal_nonBearerHeader_proceedsWithoutAuthentication() throws IOException, ServletException {
        request.setServletPath("/api/v2/protected");
        request.addHeader("Authorization", "Basic token");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, context, handlerExceptionResolver);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_validToken_setsAuthentication() throws IOException, ServletException {
        request.setServletPath("/api/v2/protected");
        request.addHeader("Authorization", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user@test.com");
        when(context.getBean(UserDetailsServiceImpl.class)).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(userDetails);
        when(jwtService.validateToken("validToken", userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername("validToken");
        verify(jwtService).validateToken("validToken", userDetails);
        verify(userDetailsService).loadUserByUsername("user@test.com");
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_nullUsername_doesNotSetAuthentication() throws IOException, ServletException {
        request.setServletPath("/api/v2/protected");
        request.addHeader("Authorization", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername("validToken");
        verifyNoInteractions(context, userDetailsService, handlerExceptionResolver);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_existingAuthentication_doesNotSetNewAuthentication() throws IOException, ServletException {
        request.setServletPath("/api/v2/protected");
        request.addHeader("Authorization", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user@test.com");
        SecurityContextHolder.getContext().setAuthentication(mock(org.springframework.security.core.Authentication.class));

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername("validToken");
        verifyNoInteractions(context, userDetailsService, handlerExceptionResolver);
    }

    @Test
    void doFilterInternal_invalidToken_doesNotSetAuthentication() throws IOException, ServletException {
        request.setServletPath("/api/v2/protected");
        request.addHeader("Authorization", "Bearer invalidToken");
        when(jwtService.extractUsername("invalidToken")).thenReturn("user@test.com");
        when(context.getBean(UserDetailsServiceImpl.class)).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(userDetails);
        when(jwtService.validateToken("invalidToken", userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).validateToken("invalidToken", userDetails);
        verify(userDetailsService).loadUserByUsername("user@test.com");
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_authenticationException_triggersHandler() {
        request.setServletPath("/api/v2/protected");
        request.addHeader("Authorization", "Bearer validToken");
        AuthenticationException exception = new AuthenticationException("Auth error") {};
        when(jwtService.extractUsername("validToken")).thenThrow(exception);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(handlerExceptionResolver).resolveException(request, response, null, exception);
        verifyNoInteractions(filterChain, context);
    }

    @Test
    void doFilterInternal_beansException_triggersHandler() {
        request.setServletPath("/api/v2/protected");
        request.addHeader("Authorization", "Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user@test.com");
        BeansException exception = new BeansException("Bean error") {};
        when(context.getBean(UserDetailsServiceImpl.class)).thenThrow(exception);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(handlerExceptionResolver).resolveException(request, response, null, exception);
        verifyNoInteractions(filterChain, userDetailsService);
    }

    @Test
    void doFilterInternal_runtimeException_triggersHandler() {
        request.setServletPath("/api/v2/protected");
        request.addHeader("Authorization", "Bearer validToken");
        RuntimeException exception = new RuntimeException("Runtime error");
        when(jwtService.extractUsername("validToken")).thenThrow(exception);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(handlerExceptionResolver).resolveException(request, response, null, exception);
        verifyNoInteractions(filterChain, context);
    }
}
