package com.bartolome.aitor.security;

import com.bartolome.aitor.model.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Duración de tokens (en milisegundos)
    private final long accessTokenExpirationMs = 24 * 60 * 60 * 1000;   // 24 horas
    private final long refreshTokenExpirationMs = 7 * 24 * 60 * 60 * 1000; // 7 días

    /**
     * Genera un token JWT de acceso con el rol incluido.
     */
    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("rol", user.getRol().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Genera un refresh token (sin incluir claims innecesarios).
     */
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Devuelve true si el refresh token es válido y no ha expirado.
     */
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Devuelve true si el access token es válido y no está expirado.
     */
    public boolean validateJwtToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Extrae el nombre de usuario (email) del token.
     */
    public String getUserNameFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extrae el rol del token.
     */
    public String getRolFromJwtToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("rol", String.class));
    }

    /**
     * Extrae cualquier claim específico.
     */
    public String getClaimFromToken(String token, String claimName) {
        return getClaimFromToken(token, claims -> claims.get(claimName, String.class));
    }

    /**
     * Extrae un claim mediante función personalizada.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene todos los claims del token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
