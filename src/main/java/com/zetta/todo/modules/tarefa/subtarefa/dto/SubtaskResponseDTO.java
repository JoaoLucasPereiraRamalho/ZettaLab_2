package com.zetta.todo.modules.tarefa.subtarefa.dto;

import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
import lombok.Data;

@Data
public class SubtaskResponseDTO {
    private Long id;
    private String description;
    private TaskStatus status;
    private Long taskId;
}