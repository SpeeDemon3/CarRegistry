package com.aruiz.CarRegistry.filter;

import com.aruiz.CarRegistry.service.impl.JwtService;
import com.aruiz.CarRegistry.service.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserServiceImpl userService;

    /**
     * Filtra las solicitudes HTTP para la autenticación basada en tokens JWT.
     *
     * Este método se encarga de procesar las solicitudes HTTP para la autenticación basada en tokens JWT.
     * Verifica si hay un token JWT en el encabezado de autorización de la solicitud,
     * lo valida y autentica al usuario si el token es válido.
     *
     * @param request     La solicitud HTTP entrante.
     * @param response    La respuesta HTTP saliente.
     * @param filterChain La cadena de filtros para continuar con el procesamiento de la solicitud.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException      Si ocurre un error de entrada/salida durante el procesamiento de la solicitud.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtenemos el encabezado de autorización
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Verificamos si el encabezado de autorización está presente
        if (StringUtils.isEmpty(authHeader)) {
            // Si no hay encabezado de autorización, continuamos con la cadena de filtros
            filterChain.doFilter(request, response);
            return;
        }

        // Extraemos el token JWT de la cadena de autorización
        jwt = authHeader.substring(7); // Bearer XXXX
        log.info("JWT -> {}", jwt);

        // Extraemos el email de usuario del token JWT
        userEmail = jwtService.extractUserName(jwt);

        // Verificamos si el token JWT es válido y si el usuario no está autenticado actualmente
        if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Cargamos los detalles del usuario desde la base de datos
            UserDetails userDetails = userService.loadUserByUsername(userEmail);

            // Verificamos si el token JWT es válido para el usuario actual
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("User - {}", userDetails);

                // Creamos un contexto de seguridad vacío
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                // Creamos un objeto de autenticación utilizando los detalles del usuario y los roles
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // Establecemos los detalles de la autenticación
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Establecemos la autenticación en el contexto de seguridad
                securityContext.setAuthentication(authenticationToken);
                // Establecemos el contexto de seguridad en el contexto actual
                SecurityContextHolder.setContext(securityContext);
            }
        }
        // Continuamos con la cadena de filtros
        filterChain.doFilter(request, response);

    }
}
