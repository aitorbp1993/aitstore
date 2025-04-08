package com.bartolome.aitor.model.entities;

import com.bartolome.aitor.model.entities.OrderItem;
import com.bartolome.aitor.model.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Pedido realizado por un usuario")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del pedido")
    private Long id;

    @Schema(description = "Fecha de creación del pedido")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Total del pedido")
    private Double total;

    @Schema(description = "Estado del pedido", example = "PENDIENTE, ENVIADO, COMPLETADO")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
