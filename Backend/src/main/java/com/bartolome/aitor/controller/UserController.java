package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.UpdateUserProfileDTO;
import com.bartolome.aitor.dto.UserDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios")
    public List<UserDTO> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID")
    public ResponseEntity<UserDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                service.obtenerPorId(id)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.obtenerPorId(id).isEmpty()) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    @Operation(summary = "Obtener el usuario autenticado")
    public ResponseEntity<UserDTO> obtenerMiPerfil(Authentication authentication) {
        String email = authentication.getName();
        UserDTO usuario = service.obtenerPorEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        return ResponseEntity.ok(usuario);
    }

    @PatchMapping("/me")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    @Operation(summary = "Actualizar perfil del usuario autenticado (nombre, dirección y teléfono)")
    public ResponseEntity<UserDTO> actualizarPerfil(@Valid @RequestBody UpdateUserProfileDTO request, Authentication authentication) {
        String email = authentication.getName();
        UserDTO actualizado = service.actualizarPerfil(email, request);
        return ResponseEntity.ok(actualizado);
    }
}
