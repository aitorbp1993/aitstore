package com.bartolome.aitor.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa a un usuario del sistema")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String nombre;

    @Column(unique = true, nullable = false)
    @Schema(description = "Correo electrónico", example = "juan@example.com")
    private String email;

    @Schema(description = "Contraseña encriptada")
    private String password;

    @Schema(description = "Rol del usuario", example = "cliente")
    private String rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<com.bartolome.aitor.model.entities.Order> pedidos;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Cart carrito;
}

