package com.bartolome.aitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa el carrito completo de un usuario")
public class CartDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID del usuario propietario del carrito", example = "5")
    private Long usersId;

    @Valid
    @Schema(description = "Lista de productos en el carrito")
    private List<CartItemDTO> items;
}
