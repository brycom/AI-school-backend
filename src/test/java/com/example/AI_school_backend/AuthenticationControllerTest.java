package com.example.AI_school_backend;

import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.Models.User;
import com.example.Models.Dtos.RegisterUserDto;
import com.example.Services.AuthenticationService;
import com.example.Services.JwtService;
import com.example.Controllers.AuthenticationController;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        RegisterUserDto registerUserDto = new RegisterUserDto("testUser", "testPassword", "testName", "testEmail");

        when(authenticationService.signup(registerUserDto))
                .thenReturn(new User("testUser", "testPassword", "testName", "testEmail", "free"));

        ResponseEntity<?> responseEntity = authenticationController.register(registerUserDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testRegisterUserExists() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("testUser");
        registerUserDto.setPassword("testPassword");
        registerUserDto.setName("testName");
        registerUserDto.setEmail("testEmail");

        DataIntegrityViolationException exception = new DataIntegrityViolationException(
                "Duplicate entry 'testUser' for key 'user.UKob8kqyqqgmefl0aco34akdtpe'");
        doThrow(exception).when(authenticationService).signup(registerUserDto);

        ResponseEntity<?> responseEntity = authenticationController.register(registerUserDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("User already registered", responseEntity.getBody());

        verify(authenticationService).signup(registerUserDto);
    }

    @Test
    public void testLogOut() {
        ResponseEntity<?> responseEntity = authenticationController.LogOut(response);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Token is valid", responseEntity.getBody());

        verify(response).addCookie(any(Cookie.class));
    }
}
