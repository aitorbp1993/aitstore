package com.bartolome.aitor.mapper;

import com.bartolome.aitor.dto.UserDTO;
import com.bartolome.aitor.model.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);

    User toEntity(UserDTO userDto);
}
