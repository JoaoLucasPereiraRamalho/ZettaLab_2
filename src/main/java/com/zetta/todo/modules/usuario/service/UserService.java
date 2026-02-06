package com.zetta.todo.modules.usuario.service;

import com.zetta.todo.common.exception.BusinessException; // Padronizado com o resto do projeto
import com.zetta.todo.modules.usuario.domain.User;
import com.zetta.todo.modules.usuario.dto.UserCreateDTO;
import com.zetta.todo.modules.usuario.dto.UserResponseDTO;
import com.zetta.todo.modules.usuario.mapper.UserMapper; // <--- Import do Mapper
import com.zetta.todo.modules.usuario.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        User user = userMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user = userRepository.save(user);

        return userMapper.toResponseDTO(user);
    }
}