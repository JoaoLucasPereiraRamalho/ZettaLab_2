package com.zetta.todo.modules.tarefa.tarefa.dto;

import com.zetta.todo.modules.tarefa.tarefa.domain.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskCreateDTO {
    @NotBlank(message = "O título é obrigatório")
    private String title;

    private String description;

    @FutureOrPresent(message = "A data de vencimento não pode ser no passado")
    private LocalDateTime dueDate;

    private TaskPriority priority;

    @NotNull(message = "A categoria é obrigatória")
    private Long categoryId;
}