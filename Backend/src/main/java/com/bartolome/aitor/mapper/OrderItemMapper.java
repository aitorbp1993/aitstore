package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.OrderItemDTO;
import com.bartolome.aitor.model.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    OrderItemDTO toDto(OrderItem item);

    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    OrderItem toEntity(OrderItemDTO dto);
}
