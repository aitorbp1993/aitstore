package com.bartolome.aitor.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaConProductosDTO {
    private String nombreCategoria;
    private List<ProductResponseDTO> productos;
}
