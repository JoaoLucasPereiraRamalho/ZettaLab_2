package com.zetta.todo.modules.tarefa.tarefa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCreateDTO {
    @NotBlank(message = "O título é obrigatório")
    private String title;

    private String description;

    private LocalDateTime dueDate; // Formato esperado: "2026-02-04T10:00:00"

    @NotNull(message = "A categoria é obrigatória")
    private Long categoryId;
}