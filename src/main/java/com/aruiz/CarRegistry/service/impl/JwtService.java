package com.aruiz.CarRegistry.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio para la gestión de tokens JWT.
 */
@Service
public class JwtService {

    // Clave secreta utilizada para firmar y verificar tokens JWT
    @Value("${token.secret.key}")
    String jwtSecretKey;

    // Tiempo de expiración del token JWT en milisegundos
    @Value("${token.expirationms}")
    Long jwtExpirationMs;

    /**
     * Extrae el nombre de usuario del token JWT.
     * @param token El token JWT del cual extraer el nombre de usuario.
     * @return El nombre de usuario extraído del token JWT.
     */
    public String extractUserName(String token) {
        return extractClain(token, Claims::getSubject);
    }

    /**
     * Verifica si un token JWT es válido para un usuario específico.
     * @param token El token JWT a verificar.
     * @param userDetails Los detalles del usuario para los cuales se verifica el token.
     * @return `true` si el token es válido para el usuario dado, de lo contrario, `false`.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Extrae una reclamación específica del token JWT.
     * @param token El token JWT del cual extraer la reclamación.
     * @param <T> El tipo de dato de la reclamación a extraer.
     * @return La reclamación extraída del token JWT.
     */
    private <T> T extractClain(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Genera un token JWT para los detalles del usuario proporcionados.
     * @param userDetails Los detalles del usuario para los cuales generar el token.
     * @return El token JWT generado.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con reclamaciones adicionales para los detalles del usuario proporcionados.
     * @param extraClaims Reclamaciones adicionales a incluir en el token.
     * @param userDetails Los detalles del usuario para los cuales generar el token.
     * @return El token JWT generado.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Establece las reclamaciones adicionales en el token
                .setSubject(userDetails.getUsername()) // Establece el email del usuario como sujeto del token
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Establece la fecha de emisión del token como la fecha actual
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Establece la fecha de expiración del token
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma el token con la clave de firma utilizando el algoritmo HS256
                .compact(); // Compacta el token en su forma final
    }

    /**
     * Verifica si un token JWT ha expirado.
     * @param token El token JWT a verificar.
     * @return `true` si el token ha expirado, de lo contrario `false`.
     */
    private boolean isTokenExpired(String token) {
        // Compara la fecha de expiración del token con la fecha actual
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     * @param token El token JWT del cual extraer la fecha de expiración.
     * @return La fecha de expiración extraída del token JWT.
     */
    private Date extractExpiration(String token) {
        // Extrae la fecha de expiración del token JWT utilizando el resolver de reclamaciones
        return extractClain(token, Claims::getExpiration);
    }

    /**
     * Extrae todas las reclamaciones del token JWT.
     * @param token El token JWT del cual extraer las reclamaciones.
     * @return Las reclamaciones extraídas del token JWT.
     */
    private Claims extractAllClaims(String token) {
        // Construye un analizador de tokens JWT, configura la clave de firma y extrae las reclamaciones del cuerpo del token
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    /**
     * Obtiene la clave de firma utilizada para firmar y verificar tokens JWT.
     * @return La clave de firma.
     */
    private Key getSigningKey() {
        // Decodifica la clave secreta del token desde Base64 y la utiliza para crear una clave HMAC-SHA para firmar y verificar tokens JWT
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
