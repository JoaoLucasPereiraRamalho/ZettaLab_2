package com.zetta.todo.modules.tarefa.subtarefa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubtaskCreateDTO {
    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @NotNull(message = "O ID da tarefa pai é obrigatório")
    private Long taskId;
}