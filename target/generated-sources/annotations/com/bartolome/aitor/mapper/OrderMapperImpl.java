package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.OrderDTO;
import com.bartolome.aitor.dto.OrderItemDTO;
import com.bartolome.aitor.model.entities.Order;
import com.bartolome.aitor.model.entities.OrderItem;
import com.bartolome.aitor.model.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T18:36:01+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderDTO toDto(Order entity) {
        if ( entity == null ) {
            return null;
        }

        OrderDTO.OrderDTOBuilder orderDTO = OrderDTO.builder();

        orderDTO.usuarioId( entityUsuarioId( entity ) );
        orderDTO.id( entity.getId() );
        orderDTO.fechaCreacion( entity.getFechaCreacion() );
        orderDTO.total( entity.getTotal() );
        orderDTO.estado( entity.getEstado() );
        orderDTO.items( orderItemListToOrderItemDTOList( entity.getItems() ) );

        return orderDTO.build();
    }

    @Override
    public Order toEntity(OrderDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.id( dto.getId() );
        order.fechaCreacion( dto.getFechaCreacion() );
        order.total( dto.getTotal() );
        order.estado( dto.getEstado() );

        return order.build();
    }

    private Long entityUsuarioId(Order order) {
        if ( order == null ) {
            return null;
        }
        User usuario = order.getUsuario();
        if ( usuario == null ) {
            return null;
        }
        Long id = usuario.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<OrderItemDTO> orderItemListToOrderItemDTOList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDTO> list1 = new ArrayList<OrderItemDTO>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemMapper.toDto( orderItem ) );
        }

        return list1;
    }
}
