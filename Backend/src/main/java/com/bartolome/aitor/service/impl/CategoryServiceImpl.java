package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.CategoriaConProductosDTO;
import com.bartolome.aitor.dto.CategoryDTO;
import com.bartolome.aitor.dto.ProductResponseDTO;
import com.bartolome.aitor.mapper.CategoryMapper;
import com.bartolome.aitor.mapper.ProductResponseMapper;
import com.bartolome.aitor.model.entities.Category;
import com.bartolome.aitor.repository.CategoryRepository;
import com.bartolome.aitor.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final ProductResponseMapper productResponseMapper;


    @Override
    public List<CategoryDTO> obtenerTodas() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public CategoryDTO guardar(CategoryDTO dto) {
        Category categoria = mapper.toEntity(dto);
        return mapper.toDto(repository.save(categoria));
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<CategoriaConProductosDTO> obtenerConProductos(Long id) {
        return repository.findById(id).map(categoria ->
                CategoriaConProductosDTO.builder()
                        .nombreCategoria(categoria.getNombre())
                        .productos(productResponseMapper.toResponseDtoList(categoria.getProductos()))
                        .build()
        );
    }
    }


