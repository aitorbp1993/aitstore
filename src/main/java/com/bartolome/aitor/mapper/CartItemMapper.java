package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.CartItemDTO;
import com.bartolome.aitor.model.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    @Mapping(source = "producto.precio", target = "precioUnitario")
    CartItemDTO toDto(CartItem item);

    @Mapping(target = "carrito", ignore = true)
    @Mapping(target = "producto", ignore = true)
    CartItem toEntity(CartItemDTO dto);
}
