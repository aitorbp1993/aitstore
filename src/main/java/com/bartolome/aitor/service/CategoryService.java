package com.bartolome.aitor.service;

import com.bartolome.aitor.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryDTO> obtenerTodas();

    Optional<CategoryDTO> obtenerPorId(Long id);

    CategoryDTO guardar(CategoryDTO dto);

    void eliminar(Long id);
}
