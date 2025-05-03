package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.ProductResponseDTO;
import com.bartolome.aitor.model.entities.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T18:36:01+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ProductResponseMapperImpl implements ProductResponseMapper {

    @Override
    public ProductResponseDTO toResponseDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponseDTO.ProductResponseDTOBuilder productResponseDTO = ProductResponseDTO.builder();

        productResponseDTO.nombre( product.getNombre() );
        productResponseDTO.precio( product.getPrecio() );
        productResponseDTO.imagenUrl( product.getImagenUrl() );
        productResponseDTO.categoria( map( product.getCategoria() ) );
        productResponseDTO.id( product.getId() );
        productResponseDTO.descripcion( product.getDescripcion() );

        return productResponseDTO.build();
    }

    @Override
    public List<ProductResponseDTO> toResponseDtoList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductResponseDTO> list = new ArrayList<ProductResponseDTO>( products.size() );
        for ( Product product : products ) {
            list.add( toResponseDto( product ) );
        }

        return list;
    }
}
