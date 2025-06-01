package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.ProductDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.mapper.ProductMapper;
import com.bartolome.aitor.model.entities.Category;
import com.bartolome.aitor.model.entities.Product;
import com.bartolome.aitor.repository.CategoryRepository;
import com.bartolome.aitor.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;
    private ProductDTO productDTO1;
    private ProductDTO productDTO2;
    private List<Product> productList;
    private List<ProductDTO> productDTOList;
    private Category category;

    @BeforeEach
    void setUp() {
        // Create test data - Category
        category = Category.builder()
                .id(1L)
                .nombre("Tarjetas gráficas")
                .build();

        // Create test data - Products
        product1 = Product.builder()
                .id(1L)
                .nombre("NVIDIA RTX 4080")
                .descripcion("Tarjeta gráfica de alta gama")
                .precio(999.99)
                .stock(10)
                .imagenUrl("http://example.com/rtx4080.jpg")
                .categoria(category)
                .build();

        product2 = Product.builder()
                .id(2L)
                .nombre("AMD Radeon RX 7900 XT")
                .descripcion("Tarjeta gráfica de alta gama de AMD")
                .precio(899.99)
                .stock(15)
                .imagenUrl("http://example.com/rx7900xt.jpg")
                .categoria(category)
                .build();

        productList = Arrays.asList(product1, product2);

        // Create test data - ProductDTOs
        productDTO1 = ProductDTO.builder()
                .id(1L)
                .nombre("NVIDIA RTX 4080")
                .descripcion("Tarjeta gráfica de alta gama")
                .precio(999.99)
                .stock(10)
                .imagenUrl("http://example.com/rtx4080.jpg")
                .categoria("Tarjetas gráficas")
                .build();

        productDTO2 = ProductDTO.builder()
                .id(2L)
                .nombre("AMD Radeon RX 7900 XT")
                .descripcion("Tarjeta gráfica de alta gama de AMD")
                .precio(899.99)
                .stock(15)
                .imagenUrl("http://example.com/rx7900xt.jpg")
                .categoria("Tarjetas gráficas")
                .build();

        productDTOList = Arrays.asList(productDTO1, productDTO2);
    }

    @Test
    @DisplayName("Debería obtener todos los productos")
    void obtenerTodosDeberiaRetornarTodosLosProductos() {
        // Given
        when(productRepository.findAll()).thenReturn(productList);
        when(productMapper.toDto(product1)).thenReturn(productDTO1);
        when(productMapper.toDto(product2)).thenReturn(productDTO2);

        // When
        List<ProductDTO> result = productService.obtenerTodos();

        // Then
        assertEquals(2, result.size());
        assertEquals(productDTO1, result.get(0));
        assertEquals(productDTO2, result.get(1));
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).toDto(product1);
        verify(productMapper, times(1)).toDto(product2);
    }

    @Test
    @DisplayName("Debería obtener un producto por ID")
    void obtenerPorIdDeberiaRetornarProducto() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productMapper.toDto(product1)).thenReturn(productDTO1);

        // When
        Optional<ProductDTO> result = productService.obtenerPorId(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(productDTO1, result.get());
        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, times(1)).toDto(product1);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando el producto no existe")
    void obtenerPorIdDeberiaRetornarOptionalVacioCuandoProductoNoExiste() {
        // Given
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<ProductDTO> result = productService.obtenerPorId(99L);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(99L);
        verify(productMapper, never()).toDto(any(Product.class));
    }

    @Test
    @DisplayName("Debería guardar un producto")
    void guardarDeberiaCrearProducto() {
        // Given
        Product productToSave = Product.builder()
                .nombre("NVIDIA RTX 4080")
                .descripcion("Tarjeta gráfica de alta gama")
                .precio(999.99)
                .stock(10)
                .imagenUrl("http://example.com/rtx4080.jpg")
                .build();

        when(productMapper.toEntity(productDTO1)).thenReturn(productToSave);
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        when(productRepository.save(any(Product.class))).thenReturn(product1);
        when(productMapper.toDto(product1)).thenReturn(productDTO1);

        // When
        ProductDTO result = productService.guardar(productDTO1);

        // Then
        assertEquals(productDTO1, result);
        verify(productMapper, times(1)).toEntity(productDTO1);
        verify(categoryRepository, times(1)).findAll();
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toDto(product1);
    }

    @Test
    @DisplayName("Debería lanzar excepción al guardar un producto con categoría inexistente")
    void guardarDeberiaLanzarExcepcionCuandoCategoriaNoExiste() {
        // Given
        ProductDTO productDTOWithInvalidCategory = ProductDTO.builder()
                .nombre("NVIDIA RTX 4080")
                .descripcion("Tarjeta gráfica de alta gama")
                .precio(999.99)
                .stock(10)
                .imagenUrl("http://example.com/rtx4080.jpg")
                .categoria("Categoría Inexistente")
                .build();

        Product productToSave = Product.builder()
                .nombre("NVIDIA RTX 4080")
                .descripcion("Tarjeta gráfica de alta gama")
                .precio(999.99)
                .stock(10)
                .imagenUrl("http://example.com/rtx4080.jpg")
                .build();

        when(productMapper.toEntity(productDTOWithInvalidCategory)).thenReturn(productToSave);
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            productService.guardar(productDTOWithInvalidCategory);
        });

        verify(productMapper, times(1)).toEntity(productDTOWithInvalidCategory);
        verify(categoryRepository, times(1)).findAll();
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Debería eliminar un producto")
    void eliminarDeberiaEliminarProducto() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        // When
        productService.eliminar(1L);

        // Then
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debería lanzar excepción al eliminar un producto que no existe")
    void eliminarDeberiaLanzarExcepcionCuandoProductoNoExiste() {
        // Given
        when(productRepository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            productService.eliminar(99L);
        });

        verify(productRepository, times(1)).existsById(99L);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Debería actualizar un producto")
    void actualizarDeberiaActualizarProducto() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        when(productRepository.save(any(Product.class))).thenReturn(product1);
        when(productMapper.toDto(product1)).thenReturn(productDTO1);

        // When
        ProductDTO result = productService.actualizar(1L, productDTO1);

        // Then
        assertEquals(productDTO1, result);
        verify(productRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findAll();
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toDto(product1);
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar un producto que no existe")
    void actualizarDeberiaLanzarExcepcionCuandoProductoNoExiste() {
        // Given
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            productService.actualizar(99L, productDTO1);
        });

        verify(productRepository, times(1)).findById(99L);
        verify(categoryRepository, never()).findAll();
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Debería eliminar productos sin stock")
    void eliminarProductosSinStockDeberiaEliminarProductos() {
        // Given
        Product productWithoutStock = Product.builder()
                .id(3L)
                .nombre("Producto sin stock")
                .descripcion("Producto que no tiene stock")
                .precio(99.99)
                .stock(0)
                .imagenUrl("http://example.com/nostock.jpg")
                .categoria(category)
                .build();

        List<Product> allProducts = Arrays.asList(product1, product2, productWithoutStock);
        List<Product> productsWithoutStock = Arrays.asList(productWithoutStock);

        when(productRepository.findAll()).thenReturn(allProducts);
        doNothing().when(productRepository).deleteAll(productsWithoutStock);

        // When
        productService.eliminarProductosSinStock();

        // Then
        verify(productRepository, times(1)).findAll();
        verify(productRepository, times(1)).deleteAll(anyList());
    }

    @Test
    @DisplayName("No debería eliminar productos cuando todos tienen stock")
    void eliminarProductosSinStockNoDeberiaEliminarProductosCuandoTodosTienenStock() {
        // Given
        when(productRepository.findAll()).thenReturn(productList); // Todos tienen stock > 0

        // When
        productService.eliminarProductosSinStock();

        // Then
        verify(productRepository, times(1)).findAll();
        verify(productRepository, never()).deleteAll(anyList());
    }
}