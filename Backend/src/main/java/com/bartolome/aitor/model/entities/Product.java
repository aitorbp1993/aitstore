package com.bartolome.aitor.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa un producto disponible en la tienda")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "NVIDIA RTX 4080")
    private String nombre;

    @Schema(description = "Descripción del producto")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "999.99")
    private Double precio;

    @Schema(description = "Cantidad disponible en stock", example = "20")
    private Integer stock;

    @Schema(description = "URL de la imagen del producto")
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> carritoItems;


}
