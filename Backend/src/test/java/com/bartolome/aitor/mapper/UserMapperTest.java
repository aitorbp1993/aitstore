package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.UserDTO;
import com.bartolome.aitor.model.entities.User;
import com.bartolome.aitor.model.enums.Rol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Debería mapear User a UserDTO correctamente")
    void toDtoDeberiaMapearUserAUserDTOCorrectamente() {
        // Given
        User user = User.builder()
                .id(1L)
                .nombre("Usuario Test")
                .email("test@example.com")
                .password("password123")
                .rol(Rol.CLIENTE)
                .direccion("Calle Test 123")
                .telefono("123456789")
                .build();

        // When
        UserDTO userDTO = userMapper.toDto(user);

        // Then
        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getNombre(), userDTO.getNombre());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getPassword(), userDTO.getPassword());
        assertEquals(user.getRol(), userDTO.getRol());
        assertEquals(user.getDireccion(), userDTO.getDireccion());
        assertEquals(user.getTelefono(), userDTO.getTelefono());
    }

    @Test
    @DisplayName("Debería mapear UserDTO a User correctamente")
    void toEntityDeberiaMapearUserDTOAUserCorrectamente() {
        // Given
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .nombre("Usuario Test")
                .email("test@example.com")
                .password("password123")
                .rol(Rol.CLIENTE)
                .direccion("Calle Test 123")
                .telefono("123456789")
                .build();

        // When
        User user = userMapper.toEntity(userDTO);

        // Then
        assertNotNull(user);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getNombre(), user.getNombre());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getPassword(), user.getPassword());
        assertEquals(userDTO.getRol(), user.getRol());
        assertEquals(userDTO.getDireccion(), user.getDireccion());
        assertEquals(userDTO.getTelefono(), user.getTelefono());
    }

    @Test
    @DisplayName("Debería manejar valores nulos correctamente")
    void deberiaManejearValoresNulosCorrectamente() {
        // Given
        User userWithNulls = User.builder()
                .id(1L)
                .nombre("Usuario Test")
                .email("test@example.com")
                .password("password123")
                .rol(Rol.CLIENTE)
                .direccion(null)
                .telefono(null)
                .build();

        UserDTO userDTOWithNulls = UserDTO.builder()
                .id(2L)
                .nombre("Usuario DTO Test")
                .email("testdto@example.com")
                .password("password456")
                .rol(Rol.ADMIN)
                .direccion(null)
                .telefono(null)
                .build();

        // When
        UserDTO mappedDTO = userMapper.toDto(userWithNulls);
        User mappedEntity = userMapper.toEntity(userDTOWithNulls);

        // Then
        assertNotNull(mappedDTO);
        assertNull(mappedDTO.getDireccion());
        assertNull(mappedDTO.getTelefono());

        assertNotNull(mappedEntity);
        assertNull(mappedEntity.getDireccion());
        assertNull(mappedEntity.getTelefono());
    }
}