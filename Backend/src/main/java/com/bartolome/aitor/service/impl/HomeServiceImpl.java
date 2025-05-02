
package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.CategoriaConProductosDTO;
import com.bartolome.aitor.mapper.ProductResponseMapper;
import com.bartolome.aitor.model.entities.Category;
import com.bartolome.aitor.repository.CategoryRepository;
import com.bartolome.aitor.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final CategoryRepository categoryRepository;
    private final ProductResponseMapper productMapper;

    @Override
    public List<CategoriaConProductosDTO> obtenerCategoriasConProductos() {
        List<Category> categorias = categoryRepository.findAll()
                .stream()
                .limit(4) // Elegimos las primeras 4 categorías
                .collect(Collectors.toList());

        return categorias.stream()
                .map(categoria -> CategoriaConProductosDTO.builder()
                        .nombreCategoria(categoria.getNombre())
                        .productos(
                                categoria.getProductos()
                                        .stream()
                                        .limit(4)
                                        .map(productMapper::toResponseDto)
                                        .collect(Collectors.toList())
                        )
                        .build())
                .collect(Collectors.toList());
    }
}
