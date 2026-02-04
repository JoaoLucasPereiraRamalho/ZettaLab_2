package com.zetta.todo.modules.usuario.service;

import com.zetta.todo.modules.usuario.domain.User;
import com.zetta.todo.modules.usuario.dto.LoginRequestDTO;
import com.zetta.todo.modules.usuario.dto.LoginResponseDTO;
import com.zetta.todo.modules.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        String token = tokenService.generateToken(user);

        return new LoginResponseDTO(token);
    }
}
