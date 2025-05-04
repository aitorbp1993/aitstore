package com.bartolome.aitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa un producto dentro de un pedido")
public class OrderItemDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    private String nombreProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debe haber al menos una unidad")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    private Double precioUnitario;
}
