package com.zetta.todo.modules.tarefa.subtarefa.mapper;

import com.zetta.todo.modules.tarefa.subtarefa.domain.Subtask;
import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskCreateDTO;
import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskResponseDTO;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class SubtaskMapper {

    public SubtaskResponseDTO toResponseDTO(Subtask subtask) {
        SubtaskResponseDTO dto = new SubtaskResponseDTO();
        dto.setId(subtask.getId());
        dto.setDescription(subtask.getDescription());
        dto.setStatus(subtask.getStatus());

        if (subtask.getTask() != null) {
            dto.setTaskId(subtask.getTask().getId());
        }

        return dto;
    }

    public Subtask toEntity(SubtaskCreateDTO dto) {
        Subtask subtask = new Subtask();
        subtask.setDescription(dto.getDescription());

        subtask.setStatus(TaskStatus.PENDING);

        return subtask;
    }
}