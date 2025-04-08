package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.UserDTO;
import com.bartolome.aitor.mapper.UserMapper;
import com.bartolome.aitor.model.entities.User;
import com.bartolome.aitor.repository.UserRepository;
import com.bartolome.aitor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    @Qualifier("userMapper")
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder; // Inyectado para codificar contraseñas

    @Override
    public List<UserDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public UserDTO guardar(UserDTO dto) {
        // Convierte el DTO a entidad
        User user = mapper.toEntity(dto);
        // Codifica la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        // Guarda el usuario y lo convierte a DTO
        return mapper.toDto(repository.save(user));
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
