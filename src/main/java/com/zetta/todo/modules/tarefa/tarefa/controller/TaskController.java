package com.zetta.todo.modules.tarefa.tarefa.controller;

import com.zetta.todo.modules.tarefa.tarefa.dto.TaskCreateDTO;
import com.zetta.todo.modules.tarefa.tarefa.dto.TaskResponseDTO;
import com.zetta.todo.modules.tarefa.tarefa.service.TaskService;
import com.zetta.todo.modules.tarefa.tarefa.dto.DashboardResponseDTO;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
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

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashboardResponseDTO>> dashboard() {
        var result = taskService.listByDashboard();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id, @RequestBody @Valid TaskCreateDTO dto) {
        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(@PathVariable Long id, @RequestBody TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}