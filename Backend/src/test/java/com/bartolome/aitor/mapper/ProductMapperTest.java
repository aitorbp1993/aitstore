package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.ProductDTO;
import com.bartolome.aitor.model.entities.Category;
import com.bartolome.aitor.model.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class ProductMapperTest {

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    @DisplayName("Debería mapear Product a ProductDTO correctamente")
    void toDtoDeberiaMapearProductAProductDTOCorrectamente() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .nombre("Tarjetas gráficas")
                .build();

        Product product = Product.builder()
                .id(1L)
                .nombre("NVIDIA RTX 4080")
                .descripcion("Tarjeta gráfica de alta gama")
                .precio(999.99)
                .stock(10)
                .imagenUrl("http://example.com/rtx4080.jpg")
                .categoria(category)
                .build();

        // When
        ProductDTO productDTO = productMapper.toDto(product);

        // Then
        assertNotNull(productDTO);
        assertEquals(product.getId(), productDTO.getId());
        assertEquals(product.getNombre(), productDTO.getNombre());
        assertEquals(product.getDescripcion(), productDTO.getDescripcion());
        assertEquals(product.getPrecio(), productDTO.getPrecio());
        assertEquals(product.getStock(), productDTO.getStock());
        assertEquals(product.getImagenUrl(), productDTO.getImagenUrl());
        assertEquals(product.getCategoria().getNombre(), productDTO.getCategoria());
    }

    @Test
    @DisplayName("Debería mapear ProductDTO a Product correctamente")
    void toEntityDeberiaMapearProductDTOAProductCorrectamente() {
        // Given
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .nombre("NVIDIA RTX 4080")
                .descripcion("Tarjeta gráfica de alta gama")
                .precio(999.99)
                .stock(10)
                .imagenUrl("http://example.com/rtx4080.jpg")
                .categoria("Tarjetas gráficas")
                .build();

        // When
        Product product = productMapper.toEntity(productDTO);

        // Then
        assertNotNull(product);
        assertEquals(productDTO.getId(), product.getId());
        assertEquals(productDTO.getNombre(), product.getNombre());
        assertEquals(productDTO.getDescripcion(), product.getDescripcion());
        assertEquals(productDTO.getPrecio(), product.getPrecio());
        assertEquals(productDTO.getStock(), product.getStock());
        assertEquals(productDTO.getImagenUrl(), product.getImagenUrl());
        // La categoría se ignora en el mapeo y se asigna en el servicio
        assertNull(product.getCategoria());
    }

    @Test
    @DisplayName("Debería manejar valores nulos correctamente")
    void deberiaManejearValoresNulosCorrectamente() {
        // Given
        Product productWithNulls = Product.builder()
                .id(1L)
                .nombre("NVIDIA RTX 4080")
                .descripcion(null)
                .precio(999.99)
                .stock(10)
                .imagenUrl(null)
                .categoria(null)
                .build();

        ProductDTO productDTOWithNulls = ProductDTO.builder()
                .id(1L)
                .nombre("NVIDIA RTX 4080")
                .descripcion(null)
                .precio(999.99)
                .stock(10)
                .imagenUrl(null)
                .categoria(null)
                .build();

        // When
        ProductDTO mappedDTO = productMapper.toDto(productWithNulls);
        Product mappedEntity = productMapper.toEntity(productDTOWithNulls);

        // Then
        assertNotNull(mappedDTO);
        assertNull(mappedDTO.getDescripcion());
        assertNull(mappedDTO.getImagenUrl());
        assertNull(mappedDTO.getCategoria());

        assertNotNull(mappedEntity);
        assertNull(mappedEntity.getDescripcion());
        assertNull(mappedEntity.getImagenUrl());
        assertNull(mappedEntity.getCategoria());
    }
}