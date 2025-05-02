package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.UserDTO;
import com.bartolome.aitor.mapper.UserMapper;
import com.bartolome.aitor.model.entities.User;
import com.bartolome.aitor.repository.UserRepository;
import com.bartolome.aitor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<UserDTO> obtenerPorEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDto);
    }

}
