package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.UpdateUserProfileDTO;
import com.bartolome.aitor.dto.UserDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.mapper.UserMapper;
import com.bartolome.aitor.model.entities.User;
import com.bartolome.aitor.repository.UserRepository;
import com.bartolome.aitor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> obtenerTodos() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public Optional<UserDTO> obtenerPorId(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public Optional<UserDTO> obtenerPorEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }

    @Override
    public void eliminar(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO actualizarPerfil(String email, UpdateUserProfileDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        user.setNombre(dto.getNombre());
        user.setDireccion(dto.getDireccion());
        user.setTelefono(dto.getTelefono());

        return userMapper.toDto(userRepository.save(user));
    }
}
