package com.bartolome.aitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa una categoría de productos")
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Schema(description = "Nombre de la categoría", example = "Placas base")
    private String nombre;
}
