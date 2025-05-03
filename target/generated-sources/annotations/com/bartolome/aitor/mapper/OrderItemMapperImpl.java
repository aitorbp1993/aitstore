package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.OrderItemDTO;
import com.bartolome.aitor.model.entities.OrderItem;
import com.bartolome.aitor.model.entities.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T18:36:01+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItemDTO toDto(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        OrderItemDTO.OrderItemDTOBuilder orderItemDTO = OrderItemDTO.builder();

        orderItemDTO.productoId( itemProductoId( item ) );
        orderItemDTO.nombreProducto( itemProductoNombre( item ) );
        orderItemDTO.cantidad( item.getCantidad() );
        orderItemDTO.precioUnitario( item.getPrecioUnitario() );

        return orderItemDTO.build();
    }

    @Override
    public OrderItem toEntity(OrderItemDTO dto) {
        if ( dto == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        orderItem.cantidad( dto.getCantidad() );
        orderItem.precioUnitario( dto.getPrecioUnitario() );

        return orderItem.build();
    }

    private Long itemProductoId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product producto = orderItem.getProducto();
        if ( producto == null ) {
            return null;
        }
        Long id = producto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String itemProductoNombre(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Product producto = orderItem.getProducto();
        if ( producto == null ) {
            return null;
        }
        String nombre = producto.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }
}
