package com.bartolome.aitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa un pedido")
public class OrderDTO {

    private Long id;

    private LocalDateTime fechaCreacion;

    @NotNull(message = "El total del pedido es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que 0")
    private Double total;

    @NotBlank(message = "El estado del pedido es obligatorio")
    private String estado;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La lista de productos no puede ser nula")
    @Size(min = 1, message = "Debe haber al menos un producto en el pedido")
    private List<@Valid OrderItemDTO> items;
}
