package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.ProductResponseDTO;
import com.bartolome.aitor.model.entities.Product;
import com.bartolome.aitor.model.entities.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {

    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "precio", target = "precio")
    @Mapping(source = "imagenUrl", target = "imagenUrl")
    @Mapping(source = "categoria", target = "categoria")
    ProductResponseDTO toResponseDto(Product product);

    List<ProductResponseDTO> toResponseDtoList(List<Product> products);

    // Método auxiliar para mapear Category a String
    default String map(Category categoria) {
        return categoria != null ? categoria.getNombre() : null;
    }
}
