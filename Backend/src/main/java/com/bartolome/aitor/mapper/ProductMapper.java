package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.ProductDTO;
import com.bartolome.aitor.model.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "categoria.nombre", target = "categoria")
    ProductDTO toDto(Product producto);

    @Mapping(target = "categoria", ignore = true) // la categoría se asignará desde el servicio
    Product toEntity(ProductDTO productoDto);
}
