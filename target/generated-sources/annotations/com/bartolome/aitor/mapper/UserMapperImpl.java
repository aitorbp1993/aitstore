package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.UserDTO;
import com.bartolome.aitor.model.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T18:36:01+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.id( user.getId() );
        userDTO.nombre( user.getNombre() );
        userDTO.email( user.getEmail() );
        userDTO.rol( user.getRol() );

        return userDTO.build();
    }

    @Override
    public User toEntity(UserDTO userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userDto.getId() );
        user.nombre( userDto.getNombre() );
        user.email( userDto.getEmail() );
        user.rol( userDto.getRol() );

        return user.build();
    }
}
