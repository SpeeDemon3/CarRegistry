package com.aruiz.CarRegistry.service.impl;

import com.aruiz.CarRegistry.controller.dto.LoginRequest;
import com.aruiz.CarRegistry.controller.dto.LoginResponse;
import com.aruiz.CarRegistry.controller.dto.SingUpRequest;
import com.aruiz.CarRegistry.entity.UserEntity;
import com.aruiz.CarRegistry.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Servicio para la autenticación y gestión de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    // Dependencias
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registro de un nuevo usuario.
     *
     * @param request Datos de registro del usuario.
     * @return Respuesta con el token JWT generado para el usuario registrado.
     * @throws Exception Si ocurre un error durante el proceso de registro.
     */
    public LoginResponse signup(SingUpRequest request) throws Exception {
        // Construir un nuevo objeto UserEntity con los datos proporcionados en la solicitud de registro
        var user = UserEntity
                .builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // Guardar el usuario en la base de datos
        user = userService.save(user);

        // Generar un token JWT para el usuario registrado
        var jwt = jwtService.generateToken(user);

        // Construir y devolver una respuesta de inicio de sesión con el token JWT generado
        return LoginResponse.builder().jwt(jwt).build();
    }

    /**
     * Inicio de sesión de un usuario existente.
     *
     * @param loginRequest Datos de inicio de sesión del usuario.
     * @return Respuesta con el token JWT generado para el usuario que ha iniciado sesión.
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // Autenticar al usuario utilizando el AuthenticationManager
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // Buscar al usuario en la base de datos por su dirección de correo electrónico
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        // Generar un token JWT para el usuario que ha iniciado sesión
        var jwt = jwtService.generateToken(user);

        // Construir y devolver una respuesta de inicio de sesión con el token JWT generado
        return LoginResponse.builder().jwt(jwt).build();
    }


}
