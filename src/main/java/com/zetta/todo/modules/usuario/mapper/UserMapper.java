package com.zetta.todo.modules.usuario.mapper;

import com.zetta.todo.modules.usuario.domain.User;
import com.zetta.todo.modules.usuario.dto.UserCreateDTO;
import com.zetta.todo.modules.usuario.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public User toEntity(UserCreateDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        user.setPassword(dto.getPassword());

        return user;
    }
}