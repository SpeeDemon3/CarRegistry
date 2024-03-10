package com.aruiz.CarRegistry.config;

import com.aruiz.CarRegistry.filter.JwtAuthenticationFilter;
import com.aruiz.CarRegistry.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAthenticationFilter;

    private final UserServiceImpl userService;

    private final PasswordEncoder passwordEncoder;

    /**
     * Configura y devuelve un proveedor de autenticación.
     *
     * Este método crea un nuevo proveedor de autenticación (DaoAuthenticationProvider)
     * y lo configura con el servicio de detalles de usuario (UserDetailsService) y el codificador de contraseñas.
     *
     * @return El proveedor de autenticación configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Creamos un nuevo proveedor de autenticación
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // Establecemos el servicio de detalles de usuario (UserDetailsService) que Spring Security utilizará para cargar
        // los detalles del usuario durante la autenticación.
        // En este caso, pasamos userService, que es una instancia de UserServiceImpl que implementa UserDetailsService.
        authenticationProvider.setUserDetailsService(userService);
        // Establecemos el codificador de contraseñas que se utilizará para comparar las contraseñas proporcionadas
        // por el usuario durante el proceso de autenticación.
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        // Devolvemos el proveedor de autenticación configurado
        return authenticationProvider;
    }

    /**
     * Configura y devuelve un objeto AuthenticationManager.
     *
     * @param configuration configuration La configuración de autenticación utilizada para construir el AuthenticationManager.
     * @return El objeto AuthenticationManager configurado para autenticar solicitudes de autenticación.
     * @throws Exception Exception Si hay un problema al obtener el AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Llamamos al método 'getAuthenticationManager()' de la configuración de autenticación para obtener el AuthenticationManager configurado.
        // Este método nos permite obtener el AuthenticationManager configurado que se utiliza en la aplicación para procesar las solicitudes de autenticación.
        return configuration.getAuthenticationManager();
    }


    /**
     * Configura y devuelve una cadena de filtros de seguridad.
     *
     * Este método configura un conjunto de filtros de seguridad para la aplicación.
     * Configura el manejo de CSRF, la gestión de sesiones y la autorización de las solicitudes HTTP.
     *
     * @param http El objeto HttpSecurity que se utiliza para configurar la seguridad HTTP.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si hay un problema al configurar la seguridad HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilita la protección CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Configura la gestión de sesiones para crear sesiones sin estado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura la autorización de las solicitudes HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite el acceso sin autenticación a las rutas /login y /signup mediante el método POST
                        .requestMatchers(HttpMethod.POST, "/api/user/signup", "/api/user/login").permitAll()
                        // Permite el acceso sin autenticación a las solicitudes GET en la ruta "/api/**".
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        // Permite el acceso sin autenticación a las solicitudes POST en la ruta "/api/**".
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        // Permite el acceso sin autenticación a las solicitudes PUT en la ruta "/api/**".
                        .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
                        // Permite el acceso sin autenticación a las solicitudes DELETE en la ruta "/api/**".
                        .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                        // Requiere autenticación para cualquier otra solicitud
                        .anyRequest().authenticated()
                )
                // Configura el proveedor de autenticación y agrega el filtro de autenticación JWT antes del filtro UsernamePasswordAuthenticationFilter
                .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Construye y devuelve la cadena de filtros de seguridad configurada
        return http.build();
    }

}
