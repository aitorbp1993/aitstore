package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.CreateOrderDTO;
import com.bartolome.aitor.dto.OrderDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Gestión de pedidos de usuarios")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @GetMapping
    @Operation(summary = "Listar todos los pedidos")
    public List<OrderDTO> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar pedidos por ID de usuario")
    public List<OrderDTO> pedidosPorUsuario(@PathVariable Long usuarioId) {
        return service.obtenerPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pedido por ID")
    public ResponseEntity<OrderDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.obtenerPorId(id)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con ID: " + id))
        );
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido")
    public ResponseEntity<OrderDTO> crear(@Valid @RequestBody CreateOrderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearPedido(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pedido")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.obtenerPorId(id).isEmpty()) {
            throw new RecursoNoEncontradoException("Pedido no encontrado con ID: " + id);
        }
        service.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }

    // 🚨 Manejador de excepción para stock insuficiente
    @ExceptionHandler(com.bartolome.aitor.exception.StockInsuficienteException.class)
    public ResponseEntity<String> manejarStockInsuficiente(com.bartolome.aitor.exception.StockInsuficienteException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
