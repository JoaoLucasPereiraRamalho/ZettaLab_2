package com.zetta.todo.modules.tarefa.tarefa.controller;

import com.zetta.todo.modules.tarefa.tarefa.dto.TaskCreateDTO;
import com.zetta.todo.modules.tarefa.tarefa.dto.TaskResponseDTO;
import com.zetta.todo.modules.tarefa.tarefa.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody @Valid TaskCreateDTO dto) {
        var task = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> list() {
        var tasks = taskService.listAll();
        return ResponseEntity.ok(tasks);
    }
}