package com.zetta.todo.modules.tarefa.subtarefa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubtaskCreateDTO {
    @NotBlank(message = "{validation.description.required}")
    private String description;

    @NotNull(message = "{validation.task.required}")
    private Long taskId;
}