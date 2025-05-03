package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.CartItemDTO;
import com.bartolome.aitor.model.entities.CartItem;
import com.bartolome.aitor.model.entities.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T18:36:01+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CartItemMapperImpl implements CartItemMapper {

    @Override
    public CartItemDTO toDto(CartItem item) {
        if ( item == null ) {
            return null;
        }

        CartItemDTO.CartItemDTOBuilder cartItemDTO = CartItemDTO.builder();

        cartItemDTO.productoId( itemProductoId( item ) );
        cartItemDTO.nombreProducto( itemProductoNombre( item ) );
        cartItemDTO.precioUnitario( itemProductoPrecio( item ) );
        cartItemDTO.cantidad( item.getCantidad() );

        return cartItemDTO.build();
    }

    @Override
    public CartItem toEntity(CartItemDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CartItem.CartItemBuilder cartItem = CartItem.builder();

        cartItem.cantidad( dto.getCantidad() );

        return cartItem.build();
    }

    private Long itemProductoId(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Product producto = cartItem.getProducto();
        if ( producto == null ) {
            return null;
        }
        Long id = producto.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String itemProductoNombre(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Product producto = cartItem.getProducto();
        if ( producto == null ) {
            return null;
        }
        String nombre = producto.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }

    private Double itemProductoPrecio(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Product producto = cartItem.getProducto();
        if ( producto == null ) {
            return null;
        }
        Double precio = producto.getPrecio();
        if ( precio == null ) {
            return null;
        }
        return precio;
    }
}
