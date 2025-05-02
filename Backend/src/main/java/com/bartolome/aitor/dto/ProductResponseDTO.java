package com.bartolome.aitor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagenUrl;
    private String categoria;
}
