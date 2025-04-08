package com.bartolome.aitor.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carrito_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Producto dentro del carrito de un usuario")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del ítem en el carrito")
    private Long id;

    @Schema(description = "Cantidad seleccionada del producto")
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Cart carrito;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product producto;
}
