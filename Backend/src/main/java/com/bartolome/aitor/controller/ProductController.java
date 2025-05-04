package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.ProductDTO;
import com.bartolome.aitor.dto.ProductResponseDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.mapper.ProductResponseMapper;
import com.bartolome.aitor.model.entities.Product;
import com.bartolome.aitor.repository.ProductRepository;
import com.bartolome.aitor.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final ProductRepository productRepository;
    private final ProductResponseMapper productResponseMapperper;

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<List<ProductResponseDTO>> listarTodos() {
        List<Product> productos = productRepository.findAll();
        List<ProductResponseDTO> productosDTO = productResponseMapperper.toResponseDtoList(productos);
        return ResponseEntity.ok(productosDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID")
    public ResponseEntity<ProductResponseDTO> buscarPorId(@PathVariable Long id) {
        Product producto = productRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
        return ResponseEntity.ok(productResponseMapperper.toResponseDto(producto));
    }

    @PostMapping
    @Operation(summary = "Crear o actualizar un producto")
    public ResponseEntity<ProductDTO> guardar(@Valid @RequestBody ProductDTO dto) {
        ProductDTO productoGuardado = service.guardar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            throw new RecursoNoEncontradoException("Producto no encontrado con ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    public ResponseEntity<ProductDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        ProductDTO existente = service.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        dto.setId(id); // aseguramos que se actualiza el correcto
        ProductDTO actualizado = service.guardar(dto);
        return ResponseEntity.ok(actualizado);
    }


}
