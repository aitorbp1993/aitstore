package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.ProductDTO;
import com.bartolome.aitor.dto.ProductResponseDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.mapper.ProductResponseMapper;
import com.bartolome.aitor.model.entities.Category;
import com.bartolome.aitor.model.entities.Product;
import com.bartolome.aitor.repository.ProductRepository;
import com.bartolome.aitor.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductResponseMapper productResponseMapper;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;
    private ProductDTO productDTO1;
    private ProductDTO productDTO2;
    private ProductResponseDTO productResponseDTO1;
    private ProductResponseDTO productResponseDTO2;
    private List<Product> productList;
    private List<ProductResponseDTO> productResponseDTOList;
    private Category category;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();

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

        // Create test data - ProductResponseDTOs
        productResponseDTO1 = ProductResponseDTO.builder()
                .id(1L)
                .nombre("NVIDIA RTX 4080")
                .descripcion("Tarjeta gráfica de alta gama")
                .precio(999.99)
                .imagenUrl("http://example.com/rtx4080.jpg")
                .categoria("Tarjetas gráficas")
                .build();

        productResponseDTO2 = ProductResponseDTO.builder()
                .id(2L)
                .nombre("AMD Radeon RX 7900 XT")
                .descripcion("Tarjeta gráfica de alta gama de AMD")
                .precio(899.99)
                .imagenUrl("http://example.com/rx7900xt.jpg")
                .categoria("Tarjetas gráficas")
                .build();

        productResponseDTOList = Arrays.asList(productResponseDTO1, productResponseDTO2);
    }

    @Test
    @DisplayName("Debería listar todos los productos")
    void listarTodosDeberiaRetornarTodosLosProductos() throws Exception {
        // Given
        when(productRepository.findAll()).thenReturn(productList);
        when(productResponseMapper.toResponseDtoList(productList)).thenReturn(productResponseDTOList);

        // When & Then
        mockMvc.perform(get("/api/productos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("NVIDIA RTX 4080")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("AMD Radeon RX 7900 XT")));

        verify(productRepository, times(1)).findAll();
        verify(productResponseMapper, times(1)).toResponseDtoList(productList);
    }

    @Test
    @DisplayName("Debería encontrar un producto por ID")
    void buscarPorIdDeberiaRetornarProducto() throws Exception {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productResponseMapper.toResponseDto(product1)).thenReturn(productResponseDTO1);

        // When & Then
        mockMvc.perform(get("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("NVIDIA RTX 4080")))
                .andExpect(jsonPath("$.precio", is(999.99)));

        verify(productRepository, times(1)).findById(1L);
        verify(productResponseMapper, times(1)).toResponseDto(product1);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el producto no existe")
    void buscarPorIdDeberiaLanzarExcepcionCuandoProductoNoExiste() throws Exception {
        // Given
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/productos/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productRepository, times(1)).findById(99L);
        verify(productResponseMapper, never()).toResponseDto(any(Product.class));
    }

    @Test
    @DisplayName("Debería guardar un producto")
    void guardarDeberiaCrearProducto() throws Exception {
        // Given
        when(productService.guardar(any(ProductDTO.class))).thenReturn(productDTO1);

        // When & Then
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("NVIDIA RTX 4080")))
                .andExpect(jsonPath("$.precio", is(999.99)));

        verify(productService, times(1)).guardar(any(ProductDTO.class));
    }

    @Test
    @DisplayName("Debería eliminar un producto")
    void eliminarDeberiaEliminarProducto() throws Exception {
        // Given
        doNothing().when(productService).eliminar(1L);

        // When & Then
        mockMvc.perform(delete("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).eliminar(1L);
    }

    @Test
    @DisplayName("Debería lanzar excepción al eliminar un producto que no existe")
    void eliminarDeberiaLanzarExcepcionCuandoProductoNoExiste() throws Exception {
        // Given
        doThrow(new RecursoNoEncontradoException("Producto no encontrado con ID: 99"))
                .when(productService).eliminar(99L);

        // When & Then
        mockMvc.perform(delete("/api/productos/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).eliminar(99L);
    }

    @Test
    @DisplayName("Debería actualizar un producto existente")
    void actualizarDeberiaActualizarProducto() throws Exception {
        // Given
        when(productService.obtenerPorId(1L)).thenReturn(Optional.of(productDTO1));
        when(productService.guardar(any(ProductDTO.class))).thenReturn(productDTO1);

        // When & Then
        mockMvc.perform(put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("NVIDIA RTX 4080")))
                .andExpect(jsonPath("$.precio", is(999.99)));

        verify(productService, times(1)).obtenerPorId(1L);
        verify(productService, times(1)).guardar(any(ProductDTO.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar un producto que no existe")
    void actualizarDeberiaLanzarExcepcionCuandoProductoNoExiste() throws Exception {
        // Given
        when(productService.obtenerPorId(99L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/productos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO1)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).obtenerPorId(99L);
        verify(productService, never()).guardar(any(ProductDTO.class));
    }

    @Test
    @DisplayName("Debería eliminar productos sin stock")
    void eliminarProductosSinStockDeberiaEliminarProductos() throws Exception {
        // Given
        doNothing().when(productService).eliminarProductosSinStock();

        // When & Then
        mockMvc.perform(delete("/api/productos/eliminar-sin-stock")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Productos sin stock eliminados correctamente"));

        verify(productService, times(1)).eliminarProductosSinStock();
    }
}