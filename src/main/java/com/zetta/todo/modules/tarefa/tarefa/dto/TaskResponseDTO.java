package com.zetta.todo.modules.tarefa.tarefa.dto;

import com.zetta.todo.modules.tarefa.categoria.dto.CategoryResponseDTO;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
import lombok.Data;
import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;

    private CategoryResponseDTO category;

    private List<SubtaskResponseDTO> subtasks;
}