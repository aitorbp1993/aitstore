package com.bartolome.aitor.model.entities;

import com.bartolome.aitor.model.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // Relación con pedidos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Order> pedidos;

    // Relación uno a uno con el carrito
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE) // Indica que, al borrar el usuario, se elimine el carrito en la base de datos
    private Cart carrito;

    @Column(length = 512)
    private String refreshToken;

}
