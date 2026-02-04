package com.zetta.todo.modules.tarefa.tarefa.dto;

import com.zetta.todo.modules.tarefa.tarefa.domain.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskCreateDTO {
    @NotBlank(message = "{validation.title.required}")
    private String title;

    private String description;

    @FutureOrPresent(message = "{validation.date.future}")
    private LocalDateTime dueDate;

    private TaskPriority priority;

    @NotNull(message = "{validation.category.required}")
    private Long categoryId;
}