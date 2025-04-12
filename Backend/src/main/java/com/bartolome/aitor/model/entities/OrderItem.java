package com.bartolome.aitor.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedido_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detalle de un producto incluido en un pedido")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del ítem del pedido")
    private Long id;

    @Schema(description = "Cantidad del producto en el pedido")
    private Integer cantidad;

    @Schema(description = "Precio del producto en el momento del pedido")
    private Double precioUnitario;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private com.bartolome.aitor.model.entities.Order pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product producto;
}
