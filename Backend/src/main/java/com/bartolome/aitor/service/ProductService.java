package com.bartolome.aitor.service;

import com.bartolome.aitor.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductDTO> obtenerTodos();

    Optional<ProductDTO> obtenerPorId(Long id);

    ProductDTO guardar(ProductDTO dto);

    void eliminar(Long id);

    ProductDTO actualizar(Long id, ProductDTO dto);

}
