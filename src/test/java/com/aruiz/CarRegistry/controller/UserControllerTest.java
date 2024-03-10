package com.aruiz.CarRegistry.controller;

import com.aruiz.CarRegistry.controller.dto.SingUpRequest;
import com.aruiz.CarRegistry.service.impl.AuthenticationService;
import com.aruiz.CarRegistry.service.impl.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;
/*
    @BeforeEach
    void setUp() {
        userController = new UserController(authenticationService);
    }
/*
    @Test
    void signup_Success() throws Exception {
        SingUpRequest singUpRequest = new SingUpRequest();
        singUpRequest.setEmail("test@test.com");
        singUpRequest.setPassword("password");

        when(authenticationService.signup(singUpRequest)).thenReturn("TOKEN");

        ResponseEntity<?> responseEntity = userController.singup(singUpRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("TOKEN", responseEntity.getBody());
        verify(authenticationService, times(1)).signup(singUpRequest);
    }

*/

    @Test
    void test_signupFailure() throws Exception {
        // Given
        SingUpRequest request = new SingUpRequest();
        // Mocking behavior
        when(authenticationService.signup(request)).thenThrow(new RuntimeException());

        // When
        ResponseEntity<?> responseEntity = userController.singup(request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(authenticationService, times(1)).signup(request);
    }
/*
    @Test
    void login_Success() {
        // Given
        LoginRequest request = new LoginRequest();

        // Mocking behavior
        when(authenticationService.login(request)).thenReturn("token");

        // When
        ResponseEntity<?> responseEntity = userController.login(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("TOKEN", responseEntity.getBody());
        verify(authenticationService, times(1)).login(request);
    }

*/

}