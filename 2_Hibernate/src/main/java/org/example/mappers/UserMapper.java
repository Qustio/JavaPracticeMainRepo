package org.example.mappers;

import org.example.dto.UserDTO;
import org.example.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDto(User user);
    User fromDto(UserDTO userDto);

    List<UserDTO> toDtoList(List<User> userList);
    List<User> fromDto(List<UserDTO> userDtoList);
}
