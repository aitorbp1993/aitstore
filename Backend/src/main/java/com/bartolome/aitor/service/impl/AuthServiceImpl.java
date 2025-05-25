package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.AuthResponse;
import com.bartolome.aitor.dto.UserRegisterDTO;
import com.bartolome.aitor.model.entities.User;
import com.bartolome.aitor.model.enums.Rol;
import com.bartolome.aitor.repository.UserRepository;
import com.bartolome.aitor.security.JwtUtil;
import com.bartolome.aitor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(UserRegisterDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico");
        }

        User user = User.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .rol(Rol.CLIENTE)
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico");
        }

        String token = jwtUtil.generateJwtToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .usuarioId(user.getId())
                .nombre(user.getNombre())
                .build();
    }
}
