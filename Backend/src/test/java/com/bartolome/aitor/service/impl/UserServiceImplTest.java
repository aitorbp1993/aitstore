package com.bartolome.aitor.service.impl;

import com.bartolome.aitor.dto.UpdateUserProfileDTO;
import com.bartolome.aitor.dto.UserDTO;
import com.bartolome.aitor.exception.RecursoNoEncontradoException;
import com.bartolome.aitor.mapper.UserMapper;
import com.bartolome.aitor.model.entities.User;
import com.bartolome.aitor.model.enums.Rol;
import com.bartolome.aitor.repository.UserRepository;
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
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private User user2;
    private UserDTO userDTO1;
    private UserDTO userDTO2;
    private List<User> userList;
    private List<UserDTO> userDTOList;
    private UpdateUserProfileDTO updateUserProfileDTO;

    @BeforeEach
    void setUp() {
        // Create test data - Users
        user1 = User.builder()
                .id(1L)
                .nombre("Usuario Test 1")
                .email("test1@example.com")
                .password("password123")
                .rol(Rol.CLIENTE)
                .direccion("Calle Test 123")
                .telefono("123456789")
                .build();

        user2 = User.builder()
                .id(2L)
                .nombre("Usuario Test 2")
                .email("test2@example.com")
                .password("password456")
                .rol(Rol.ADMIN)
                .direccion("Avenida Test 456")
                .telefono("987654321")
                .build();

        userList = Arrays.asList(user1, user2);

        // Create test data - UserDTOs
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

        // Create test data - UpdateUserProfileDTO
        updateUserProfileDTO = new UpdateUserProfileDTO();
        updateUserProfileDTO.setNombre("Nombre Actualizado");
        updateUserProfileDTO.setDireccion("Dirección Actualizada");
        updateUserProfileDTO.setTelefono("555555555");
    }

    @Test
    @DisplayName("Debería obtener todos los usuarios")
    void obtenerTodosDeberiaRetornarTodosLosUsuarios() {
        // Given
        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toDto(user1)).thenReturn(userDTO1);
        when(userMapper.toDto(user2)).thenReturn(userDTO2);

        // When
        List<UserDTO> result = userService.obtenerTodos();

        // Then
        assertEquals(2, result.size());
        assertEquals(userDTO1, result.get(0));
        assertEquals(userDTO2, result.get(1));
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(user1);
        verify(userMapper, times(1)).toDto(user2);
    }

    @Test
    @DisplayName("Debería obtener un usuario por ID")
    void obtenerPorIdDeberiaRetornarUsuario() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userMapper.toDto(user1)).thenReturn(userDTO1);

        // When
        Optional<UserDTO> result = userService.obtenerPorId(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(userDTO1, result.get());
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user1);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando el usuario no existe")
    void obtenerPorIdDeberiaRetornarOptionalVacioCuandoUsuarioNoExiste() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        Optional<UserDTO> result = userService.obtenerPorId(99L);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(99L);
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    @DisplayName("Debería obtener un usuario por email")
    void obtenerPorEmailDeberiaRetornarUsuario() {
        // Given
        when(userRepository.findByEmail("test1@example.com")).thenReturn(Optional.of(user1));
        when(userMapper.toDto(user1)).thenReturn(userDTO1);

        // When
        Optional<UserDTO> result = userService.obtenerPorEmail("test1@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals(userDTO1, result.get());
        verify(userRepository, times(1)).findByEmail("test1@example.com");
        verify(userMapper, times(1)).toDto(user1);
    }

    @Test
    @DisplayName("Debería eliminar un usuario")
    void eliminarDeberiaEliminarUsuario() {
        // Given
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.eliminar(1L);

        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debería actualizar el perfil de un usuario")
    void actualizarPerfilDeberiaActualizarUsuario() {
        // Given
        User updatedUser = User.builder()
                .id(1L)
                .nombre("Nombre Actualizado")
                .email("test1@example.com")
                .password("password123")
                .rol(Rol.CLIENTE)
                .direccion("Dirección Actualizada")
                .telefono("555555555")
                .build();

        UserDTO updatedUserDTO = UserDTO.builder()
                .id(1L)
                .nombre("Nombre Actualizado")
                .email("test1@example.com")
                .password("password123")
                .rol(Rol.CLIENTE)
                .direccion("Dirección Actualizada")
                .telefono("555555555")
                .build();

        when(userRepository.findByEmail("test1@example.com")).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(updatedUserDTO);

        // When
        UserDTO result = userService.actualizarPerfil("test1@example.com", updateUserProfileDTO);

        // Then
        assertEquals(updatedUserDTO, result);
        verify(userRepository, times(1)).findByEmail("test1@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDto(updatedUser);
    }

    @Test
    @DisplayName("Debería lanzar excepción al actualizar perfil cuando el usuario no existe")
    void actualizarPerfilDeberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        when(userRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            userService.actualizarPerfil("noexiste@example.com", updateUserProfileDTO);
        });

        verify(userRepository, times(1)).findByEmail("noexiste@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toDto(any(User.class));
    }
}