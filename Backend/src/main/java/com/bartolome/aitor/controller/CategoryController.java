package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.CategoriaConProductosDTO;
import com.bartolome.aitor.dto.CategoryDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorías", description = "Operaciones sobre las categorías de productos")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public List<CategoryDTO> listar() {
        return service.obtenerTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría por ID")
    public ResponseEntity<CategoryDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.obtenerPorId(id)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id))
        );
    }

    @PostMapping
    @Operation(summary = "Crear o actualizar una categoría")
    public ResponseEntity<CategoryDTO> guardar(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.obtenerPorId(id).isEmpty()) {
            throw new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id);
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/productos")
    @Operation(summary = "Obtener una categoría con sus productos")
    public ResponseEntity<CategoriaConProductosDTO> obtenerCategoriaConProductos(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerConProductos(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id)));
    }

}
