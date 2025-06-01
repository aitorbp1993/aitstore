package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.ProductResponseDTO;
import com.bartolome.aitor.model.entities.Category;
import com.bartolome.aitor.model.entities.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductResponseMapperTest {

    private final ProductResponseMapper productResponseMapper = Mappers.getMapper(ProductResponseMapper.class);

    @Test
    @DisplayName("Debería mapear Product a ProductResponseDTO correctamente")
    void toResponseDtoDeberiaMapearProductAProductResponseDTOCorrectamente() {
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
        ProductResponseDTO productResponseDTO = productResponseMapper.toResponseDto(product);

        // Then
        assertNotNull(productResponseDTO);
        assertEquals(product.getId(), productResponseDTO.getId());
        assertEquals(product.getNombre(), productResponseDTO.getNombre());
        assertEquals(product.getDescripcion(), productResponseDTO.getDescripcion());
        assertEquals(product.getPrecio(), productResponseDTO.getPrecio());
        assertEquals(product.getImagenUrl(), productResponseDTO.getImagenUrl());
        assertEquals(product.getCategoria().getNombre(), productResponseDTO.getCategoria());
    }

    @Test
    @DisplayName("Debería mapear lista de Products a lista de ProductResponseDTOs correctamente")
    void toResponseDtoListDeberiaMapearListaDeProductsAListaDeProductResponseDTOs() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .nombre("Tarjetas gráficas")
                .build();

        Product product1 = Product.builder()
                .id(1L)
                .nombre("NVIDIA RTX 4080")
                .descripcion("Tarjeta gráfica de alta gama")
                .precio(999.99)
                .stock(10)
                .imagenUrl("http://example.com/rtx4080.jpg")
                .categoria(category)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .nombre("AMD Radeon RX 7900 XT")
                .descripcion("Tarjeta gráfica de alta gama de AMD")
                .precio(899.99)
                .stock(15)
                .imagenUrl("http://example.com/rx7900xt.jpg")
                .categoria(category)
                .build();

        List<Product> products = Arrays.asList(product1, product2);

        // When
        List<ProductResponseDTO> productResponseDTOs = productResponseMapper.toResponseDtoList(products);

        // Then
        assertNotNull(productResponseDTOs);
        assertEquals(2, productResponseDTOs.size());
        
        // Verificar el primer producto
        assertEquals(product1.getId(), productResponseDTOs.get(0).getId());
        assertEquals(product1.getNombre(), productResponseDTOs.get(0).getNombre());
        assertEquals(product1.getDescripcion(), productResponseDTOs.get(0).getDescripcion());
        assertEquals(product1.getPrecio(), productResponseDTOs.get(0).getPrecio());
        assertEquals(product1.getImagenUrl(), productResponseDTOs.get(0).getImagenUrl());
        assertEquals(product1.getCategoria().getNombre(), productResponseDTOs.get(0).getCategoria());
        
        // Verificar el segundo producto
        assertEquals(product2.getId(), productResponseDTOs.get(1).getId());
        assertEquals(product2.getNombre(), productResponseDTOs.get(1).getNombre());
        assertEquals(product2.getDescripcion(), productResponseDTOs.get(1).getDescripcion());
        assertEquals(product2.getPrecio(), productResponseDTOs.get(1).getPrecio());
        assertEquals(product2.getImagenUrl(), productResponseDTOs.get(1).getImagenUrl());
        assertEquals(product2.getCategoria().getNombre(), productResponseDTOs.get(1).getCategoria());
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

        // When
        ProductResponseDTO productResponseDTO = productResponseMapper.toResponseDto(productWithNulls);

        // Then
        assertNotNull(productResponseDTO);
        assertEquals(productWithNulls.getId(), productResponseDTO.getId());
        assertEquals(productWithNulls.getNombre(), productResponseDTO.getNombre());
        assertNull(productResponseDTO.getDescripcion());
        assertEquals(productWithNulls.getPrecio(), productResponseDTO.getPrecio());
        assertNull(productResponseDTO.getImagenUrl());
        assertNull(productResponseDTO.getCategoria());
    }

    @Test
    @DisplayName("Debería mapear Category a String correctamente")
    void mapDeberiaMapearCategoryAStringCorrectamente() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .nombre("Tarjetas gráficas")
                .build();

        // When
        String categoryName = productResponseMapper.map(category);

        // Then
        assertEquals("Tarjetas gráficas", categoryName);
    }

    @Test
    @DisplayName("Debería manejar Category nula correctamente")
    void mapDeberiaManejearCategoryNulaCorrectamente() {
        // Given
        Category category = null;

        // When
        String categoryName = productResponseMapper.map(category);

        // Then
        assertNull(categoryName);
    }
}