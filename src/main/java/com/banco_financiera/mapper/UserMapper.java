package com.banco_financiera.mapper;

import com.banco_financiera.dto.UserRequestDTO;
import com.banco_financiera.dto.UserResponseDTO;
import com.banco_financiera.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toResponseDTOList(List<User> users);

    void updateEntityFromDTO(UserRequestDTO userRequestDTO, @MappingTarget User user);
}