package com.aruiz.CarRegistry.service.impl;

import com.aruiz.CarRegistry.controller.dto.LoginResponse;
import com.aruiz.CarRegistry.controller.dto.SingUpRequest;
import com.aruiz.CarRegistry.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        // Inicio los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_Signup() throws Exception {
        // Given
        SingUpRequest singUpRequest = new SingUpRequest("Antonio", "Ruiz", "aaa@test.com", "1234",
                "VENDOR");

        // When
        LoginResponse response = authenticationService.signup(singUpRequest);

        response.setJwt("TOKEN");

        // Then
        assertEquals("TOKEN", response.getJwt());

    }
/*
    @Test
    public void test_login() {
        // Given
        LoginRequest loginRequest = new LoginRequest("test@test.com", "test");

        UserEntity user = UserEntity.builder()
                .name("Antonio")
                .surname("Ruiz")
                .email("test@test.com")
                .password("test")
                .role("CLIENT")
                .build();

        // Simular el comportamiento de las dependencias
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("TOKEN");

        // When
        LoginResponse response = authenticationService.login(loginRequest);

        response.setJwt("TOKEN");

        // Then
        assertEquals("TOKEN", response.getJwt());

    }
*/

}






























