package com.bartolome.aitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa un producto dentro del carrito de compras")
public class CartItemDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    @Schema(description = "ID del producto", example = "10")
    private Long productoId;

    @Schema(description = "Nombre del producto", example = "Intel Core i9")
    private String nombreProducto;

    @Min(value = 1, message = "Debe haber al menos una unidad")
    @Schema(description = "Cantidad del producto en el carrito", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio actual del producto", example = "349.99")
    private Double precioUnitario;
}
