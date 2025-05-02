package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.ProductDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.mapper.ProductMapper;
import com.bartolome.aitor.model.entities.Category;
import com.bartolome.aitor.model.entities.Product;
import com.bartolome.aitor.repository.CategoryRepository;
import com.bartolome.aitor.repository.ProductRepository;
import com.bartolome.aitor.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Override
    public List<ProductDTO> obtenerTodos() {
        return productRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> obtenerPorId(Long id) {
        return productRepository.findById(id)
                .map(mapper::toDto);
    }

    @Override
    public ProductDTO guardar(ProductDTO dto) {
        Product producto = mapper.toEntity(dto);

        Category categoria = categoryRepository.findAll().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(dto.getCategoria()))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con nombre: " + dto.getCategoria()));

        producto.setCategoria(categoria);
        return mapper.toDto(productRepository.save(producto));
    }


    @Override
    public void eliminar(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductDTO actualizar(Long id, ProductDTO dto) {
        Product productoExistente = productRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        productoExistente.setNombre(dto.getNombre());
        productoExistente.setDescripcion(dto.getDescripcion());
        productoExistente.setPrecio(dto.getPrecio());
        productoExistente.setStock(dto.getStock());
        productoExistente.setImagenUrl(dto.getImagenUrl());

        Category categoria = categoryRepository.findAll().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(dto.getCategoria()))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con nombre: " + dto.getCategoria()));

        productoExistente.setCategoria(categoria);

        return mapper.toDto(productRepository.save(productoExistente));
    }

}
