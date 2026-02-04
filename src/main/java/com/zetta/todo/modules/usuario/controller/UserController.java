package com.zetta.todo.modules.usuario.controller;

import com.zetta.todo.modules.usuario.dto.UserCreateDTO;
import com.zetta.todo.modules.usuario.dto.UserResponseDTO;
import com.zetta.todo.modules.usuario.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserCreateDTO dto) {
        UserResponseDTO user = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}