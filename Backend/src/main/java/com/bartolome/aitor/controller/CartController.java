package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.CartDTO;
import com.bartolome.aitor.dto.CartItemDTO;
import com.bartolome.aitor.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Operaciones del carrito de compras")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @GetMapping("/{usuarioId}")
    @Operation(summary = "Obtener el carrito de un usuario")
    public ResponseEntity<CartDTO> obtener(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerCarrito(usuarioId));
    }

    @PostMapping("/{usuarioId}/agregar")
    @Operation(summary = "Agregar un producto al carrito")
    public ResponseEntity<CartDTO> agregar(@PathVariable Long usuarioId, @Valid @RequestBody CartItemDTO item) {
        return ResponseEntity.ok(service.agregarItem(usuarioId, item));
    }

    @DeleteMapping("/{usuarioId}/eliminar/{productoId}")
    @Operation(summary = "Eliminar un producto del carrito")
    public ResponseEntity<CartDTO> eliminar(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        return ResponseEntity.ok(service.eliminarItem(usuarioId, productoId));
    }

    @DeleteMapping("/{usuarioId}/vaciar")
    @Operation(summary = "Vaciar el carrito de un usuario")
    public ResponseEntity<Void> vaciar(@PathVariable Long usuarioId) {
        service.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
