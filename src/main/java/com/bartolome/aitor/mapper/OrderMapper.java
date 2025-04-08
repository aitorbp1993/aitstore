package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.OrderDTO;
import com.bartolome.aitor.model.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    OrderDTO toDto(Order entity);

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(OrderDTO dto);
}
