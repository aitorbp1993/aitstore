package com.bartolome.aitor.service;

import com.bartolome.aitor.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> obtenerTodos();

    Optional<UserDTO> obtenerPorId(Long id);

    UserDTO guardar(UserDTO dto);

    void eliminar(Long id);
}
