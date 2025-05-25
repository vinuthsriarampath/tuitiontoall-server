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

package edu.vinu.service.auth;

import edu.vinu.model.UserPrinciple;
import edu.vinu.service.auth.impl.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserPrinciple userPrinciple;

    @Mock
    private UserDetails userDetails;

    private SecretKey key;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Set a known secret key for consistent testing
        String secretKey = Base64.getEncoder().encodeToString("test-secret-key-for-jwt-testing-1234567890".getBytes());
        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, secretKey);
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    // Test constructor with a successful key generation
    @Test
    void testConstructor_shouldInitializeSuccessfully() {
        assertNotNull(jwtService);
    }

    // Test generateToken with valid authentication
    @Test
    void testGenerateToken_shouldGenerateValidToken_whenAuthenticationIsValid() {
        // Setup
        String username = "testUser";
        String roleValue = "ROLE_INSTITUTE";
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleValue);

        when(authentication.getPrincipal()).thenReturn(userPrinciple);
        when(userPrinciple.getUsername()).thenReturn(username);
        when(userPrinciple.getAuthorities()).thenReturn((Collection) Collections.singleton(authority));

        // Act
        long beforeTokenGen = System.currentTimeMillis();
        String token = jwtService.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(token.matches("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$"), "Token should be in JWT format");

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(username, claims.getSubject());
        assertEquals(List.of(roleValue), claims.get("roles"));

        // Verify time claims
        assertNotNull(claims.getIssuedAt());
        assertTrue(beforeTokenGen >= claims.getIssuedAt().getTime() );
        assertTrue(claims.getIssuedAt().getTime() <= System.currentTimeMillis());

        assertNotNull(claims.getExpiration());
        assertEquals(claims.getIssuedAt().getTime() + 1000 * 60 * 60 * 10,
                claims.getExpiration().getTime());

        // Verify interactions
        verify(authentication).getPrincipal();
        verify(userPrinciple).getUsername();
        verify(userPrinciple).getAuthorities();
    }

    // Test generateToken with null authentication principal
    @Test
    void testGenerateToken_shouldThrowClassCastException_whenPrincipalIsNotUserPrinciple() {
        when(authentication.getPrincipal()).thenReturn(new Object());

        assertThrows(ClassCastException.class, () -> jwtService.generateToken(authentication));
        verify(authentication).getPrincipal();
    }

    // Test extractUsername with a valid token
    @Test
    void testExtractUsername_shouldReturnUsername_whenTokenIsValid() {
        String username = "testUser";
        String token = Jwts.builder()
                .subject(username)
                .signWith(key)
                .compact();

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    // Test extractUsername with an invalid token
    @Test
    void testExtractUsername_shouldThrowJwtException_whenTokenIsInvalid() {
        String invalidToken = "invalid.token.here";

        assertThrows(JwtException.class, () -> jwtService.extractUsername(invalidToken));
    }

    // Test validateToken with valid token and enabled user
    @Test
    void testValidateToken_shouldReturnTrue_whenTokenIsValidAndUserIsEnabled() {
        String username = "testUser";
        String token = Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();

        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.isEnabled()).thenReturn(true);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
        verify(userDetails).getUsername();
        verify(userDetails).isEnabled();
    }

    // Test validateToken with username mismatch
    @Test
    void testValidateToken_shouldThrowJwtException_whenUsernameDoesNotMatch() {
        String tokenUsername = "testUser";
        String userDetailsUsername = "otherUser";
        String token = Jwts.builder()
                .subject(tokenUsername)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();

        when(userDetails.getUsername()).thenReturn(userDetailsUsername);

        assertThrowsExactly(JwtException.class, () ->
                        jwtService.validateToken(token, userDetails),
                "Username dose not match with the token subject!");
        verify(userDetails).getUsername();
    }

    // Test validateToken with expired token
    @Test
    void testValidateToken_shouldThrowJwtException_whenTokenIsExpired() {
        String username = "testUser";
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis() - 2000)) // issued 2s ago
                .expiration(new Date(System.currentTimeMillis() - 1000)) // expired 1s ago
                .signWith(key)
                .compact();


        assertThrowsExactly(ExpiredJwtException.class, () ->
                        jwtService.validateToken(token, userDetails),
                "Token is Expired!");
    }

    // Test validateToken with disabled user
    @Test
    void testValidateToken_shouldThrowJwtException_whenUserIsDisabled() {
        String username = "testUser";
        String token = Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();

        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.isEnabled()).thenReturn(false);

        assertThrowsExactly(JwtException.class, () ->
                        jwtService.validateToken(token, userDetails),
                "User is Disabled!");
        verify(userDetails).getUsername();
        verify(userDetails).isEnabled();
    }

    // Test validateToken with invalid token
    @Test
    void testValidateToken_shouldThrowJwtException_whenTokenIsInvalid() {
        String invalidToken = "invalid.token.here";

        assertThrows(JwtException.class, () ->
                jwtService.validateToken(invalidToken, userDetails));
    }

}
