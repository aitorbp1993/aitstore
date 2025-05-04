package com.bartolome.aitor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaConProductosDTO {
    private String nombreCategoria;
    private List<ProductResponseDTO> productos;
}
