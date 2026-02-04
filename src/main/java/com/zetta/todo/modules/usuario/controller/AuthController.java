package com.zetta.todo.modules.usuario.controller;

import com.zetta.todo.modules.usuario.dto.LoginRequestDTO;
import com.zetta.todo.modules.usuario.dto.LoginResponseDTO;
import com.zetta.todo.modules.usuario.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO token = authService.login(dto);
        return ResponseEntity.ok(token);
    }
}