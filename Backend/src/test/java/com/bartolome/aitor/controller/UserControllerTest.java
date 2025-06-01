package com.bartolome.aitor.controller;

import com.bartolome.aitor.dto.UpdateUserProfileDTO;
import com.bartolome.aitor.dto.UserDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.model.enums.Rol;
import com.bartolome.aitor.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Authentication authentication;

    private MockMvc mockMvc;

    private UserDTO userDTO1;
    private UserDTO userDTO2;
    private List<UserDTO> userDTOList;
    private UpdateUserProfileDTO updateUserProfileDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Create test data
        userDTO1 = UserDTO.builder()
                .id(1L)
                .nombre("Usuario Test 1")
                .email("test1@example.com")
                .password("password123")
                .rol(Rol.CLIENTE)
                .direccion("Calle Test 123")
                .telefono("123456789")
                .build();

        userDTO2 = UserDTO.builder()
                .id(2L)
                .nombre("Usuario Test 2")
                .email("test2@example.com")
                .password("password456")
                .rol(Rol.ADMIN)
                .direccion("Avenida Test 456")
                .telefono("987654321")
                .build();

        userDTOList = Arrays.asList(userDTO1, userDTO2);

        updateUserProfileDTO = new UpdateUserProfileDTO();
        updateUserProfileDTO.setNombre("Nombre Actualizado");
        updateUserProfileDTO.setDireccion("Dirección Actualizada");
        updateUserProfileDTO.setTelefono("555555555");
    }

    @Test
    @DisplayName("Debería listar todos los usuarios")
    void listarDeberiaRetornarTodosLosUsuarios() throws Exception {
        // Given
        when(userService.obtenerTodos()).thenReturn(userDTOList);

        // When & Then
        mockMvc.perform(get("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Usuario Test 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("Usuario Test 2")));

        verify(userService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("Debería encontrar un usuario por ID")
    void buscarPorIdDeberiaRetornarUsuario() throws Exception {
        // Given
        when(userService.obtenerPorId(1L)).thenReturn(Optional.of(userDTO1));

        // When & Then
        mockMvc.perform(get("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Usuario Test 1")))
                .andExpect(jsonPath("$.email", is("test1@example.com")));

        verify(userService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el usuario no existe")
    void buscarPorIdDeberiaLanzarExcepcionCuandoUsuarioNoExiste() throws Exception {
        // Given
        when(userService.obtenerPorId(99L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecursoNoEncontradoException));

        verify(userService, times(1)).obtenerPorId(99L);
    }

    @Test
    @DisplayName("Debería eliminar un usuario")
    void eliminarDeberiaEliminarUsuario() throws Exception {
        // Given
        when(userService.obtenerPorId(1L)).thenReturn(Optional.of(userDTO1));
        doNothing().when(userService).eliminar(1L);

        // When & Then
        mockMvc.perform(delete("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).obtenerPorId(1L);
        verify(userService, times(1)).eliminar(1L);
    }

    @Test
    @DisplayName("Debería lanzar excepción al eliminar un usuario que no existe")
    void eliminarDeberiaLanzarExcepcionCuandoUsuarioNoExiste() throws Exception {
        // Given
        when(userService.obtenerPorId(99L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecursoNoEncontradoException));

        verify(userService, times(1)).obtenerPorId(99L);
        verify(userService, never()).eliminar(anyLong());
    }

    @Test
    @DisplayName("Debería obtener el perfil del usuario autenticado")
    void obtenerMiPerfilDeberiaRetornarUsuarioAutenticado() throws Exception {
        // Given
        when(authentication.getName()).thenReturn("test1@example.com");
        when(userService.obtenerPorEmail("test1@example.com")).thenReturn(Optional.of(userDTO1));

        // When & Then
        mockMvc.perform(get("/api/usuarios/me")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Usuario Test 1")))
                .andExpect(jsonPath("$.email", is("test1@example.com")));

        verify(authentication, atLeastOnce()).getName();
        verify(userService, times(1)).obtenerPorEmail("test1@example.com");
    }

    @Test
    @DisplayName("Debería actualizar el perfil del usuario autenticado")
    void actualizarPerfilDeberiaActualizarUsuarioAutenticado() throws Exception {
        // Given
        UserDTO updatedUserDTO = UserDTO.builder()
                .id(1L)
                .nombre("Nombre Actualizado")
                .email("test1@example.com")
                .password("password123")
                .rol(Rol.CLIENTE)
                .direccion("Dirección Actualizada")
                .telefono("555555555")
                .build();

        when(authentication.getName()).thenReturn("test1@example.com");
        when(userService.actualizarPerfil(eq("test1@example.com"), any(UpdateUserProfileDTO.class)))
                .thenReturn(updatedUserDTO);

        // When & Then
        mockMvc.perform(patch("/api/usuarios/me")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Nombre Actualizado\",\"direccion\":\"Dirección Actualizada\",\"telefono\":\"555555555\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Nombre Actualizado")))
                .andExpect(jsonPath("$.direccion", is("Dirección Actualizada")))
                .andExpect(jsonPath("$.telefono", is("555555555")));

        verify(authentication, atLeastOnce()).getName();
        verify(userService, times(1)).actualizarPerfil(eq("test1@example.com"), any(UpdateUserProfileDTO.class));
    }
}
