package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.AuthResponse;
import com.bartolome.aitor.dto.LoginRequestDTO;
import com.bartolome.aitor.dto.UserRegisterDTO;
import com.bartolome.aitor.model.entities.User;
import com.bartolome.aitor.repository.UserRepository;
import com.bartolome.aitor.security.JwtUtil;
import com.bartolome.aitor.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para login, registro y renovación de tokens")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario y generar tokens JWT")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String token = jwtUtil.generateJwtToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .usuarioId(user.getId())
                        .nombre(user.getNombre())
                        .build()
        );
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario y generar tokens JWT")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserRegisterDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar el token de acceso usando un refresh token válido")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.getUserNameFromJwtToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Validar que coincida con el último guardado en BD
        if (!refreshToken.equals(user.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtUtil.generateJwtToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .usuarioId(user.getId())
                        .nombre(user.getNombre())
                        .build()
        );
    }
}
