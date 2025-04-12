package com.bartolome.aitor.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "carritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Carrito de compras asociado a un usuario")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del carrito")
    private Long id;

    // Relación con el usuario
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)  // Esto genera ON DELETE CASCADE para la columna usuario_id
    private User usuario;

    // Otros campos, por ejemplo, la lista de items en el carrito
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL)
    private List<CartItem> items;
}
