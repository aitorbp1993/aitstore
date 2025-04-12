package com.bartolome.aitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para la creación de un nuevo pedido")
public class CreateOrderDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID del usuario que realiza el pedido", example = "3")
    private Long usuarioId;

    @NotBlank(message = "El estado del pedido es obligatorio")
    @Schema(description = "Estado inicial del pedido", example = "PENDIENTE")
    private String estado;

    @NotNull(message = "La lista de productos no puede estar vacía")
    @Size(min = 1, message = "Debe haber al menos un producto")
    @Schema(description = "Lista de productos a incluir en el pedido")
    private List<@Valid OrderItemDTO> items;
}
