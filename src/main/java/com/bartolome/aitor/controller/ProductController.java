package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.ProductDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public List<ProductDTO> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID")
    public ResponseEntity<ProductDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.obtenerPorId(id)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id))
        );
    }

    @PostMapping
    @Operation(summary = "Crear o actualizar un producto")
    public ResponseEntity<ProductDTO> guardar(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.obtenerPorId(id).isEmpty()) {
            throw new RecursoNoEncontradoException("Producto no encontrado con ID: " + id);
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
