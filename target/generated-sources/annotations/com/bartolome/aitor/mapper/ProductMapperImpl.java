package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.ProductDTO;
import com.bartolome.aitor.model.entities.Category;
import com.bartolome.aitor.model.entities.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T18:36:01+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toDto(Product producto) {
        if ( producto == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder productDTO = ProductDTO.builder();

        productDTO.categoria( productoCategoriaNombre( producto ) );
        productDTO.id( producto.getId() );
        productDTO.nombre( producto.getNombre() );
        productDTO.descripcion( producto.getDescripcion() );
        productDTO.precio( producto.getPrecio() );
        productDTO.stock( producto.getStock() );
        productDTO.imagenUrl( producto.getImagenUrl() );

        return productDTO.build();
    }

    @Override
    public Product toEntity(ProductDTO productoDto) {
        if ( productoDto == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( productoDto.getId() );
        product.nombre( productoDto.getNombre() );
        product.descripcion( productoDto.getDescripcion() );
        product.precio( productoDto.getPrecio() );
        product.stock( productoDto.getStock() );
        product.imagenUrl( productoDto.getImagenUrl() );

        return product.build();
    }

    private String productoCategoriaNombre(Product product) {
        if ( product == null ) {
            return null;
        }
        Category categoria = product.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        String nombre = categoria.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }
}
