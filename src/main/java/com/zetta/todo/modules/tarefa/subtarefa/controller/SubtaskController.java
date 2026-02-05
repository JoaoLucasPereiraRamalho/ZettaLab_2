package com.zetta.todo.modules.tarefa.subtarefa.controller;

import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskCreateDTO;
import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskResponseDTO;
import com.zetta.todo.modules.tarefa.subtarefa.service.SubtaskService;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subtasks")
@RequiredArgsConstructor
public class SubtaskController {

    private final SubtaskService subtaskService;

    @PostMapping
    public ResponseEntity<SubtaskResponseDTO> create(@RequestBody @Valid SubtaskCreateDTO dto) {
        var subtask = subtaskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(subtask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subtaskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SubtaskResponseDTO> updateStatus(@PathVariable Long id, @RequestBody TaskStatus status) {
        var subtask = subtaskService.updateStatus(id, status);
        return ResponseEntity.ok(subtask);
    }
}