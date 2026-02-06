package com.zetta.todo.modules.tarefa.tarefa.mapper;

import com.zetta.todo.modules.tarefa.categoria.dto.CategoryResponseDTO;
import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskResponseDTO;
import com.zetta.todo.modules.tarefa.tarefa.domain.Task;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskPriority;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
import com.zetta.todo.modules.tarefa.tarefa.dto.TaskCreateDTO;
import com.zetta.todo.modules.tarefa.tarefa.dto.TaskResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public TaskResponseDTO toResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setPriority(task.getPriority());

        if (task.getCategory() != null) {
            CategoryResponseDTO catDto = new CategoryResponseDTO();
            catDto.setId(task.getCategory().getId());
            catDto.setName(task.getCategory().getName());
            catDto.setColor(task.getCategory().getColor());
            dto.setCategory(catDto);
        }

        if (task.getSubtasks() != null) {
            List<SubtaskResponseDTO> subItems = task.getSubtasks().stream()
                    .map(sub -> {
                        SubtaskResponseDTO subDto = new SubtaskResponseDTO();
                        subDto.setId(sub.getId());
                        subDto.setDescription(sub.getDescription());
                        subDto.setStatus(sub.getStatus());
                        subDto.setTaskId(task.getId());
                        return subDto;
                    }).collect(Collectors.toList());
            dto.setSubtasks(subItems);
        } else {
            dto.setSubtasks(Collections.emptyList());
        }

        return dto;
    }

    public Task toEntity(TaskCreateDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());

        task.setStatus(TaskStatus.PENDING);
        task.setPriority(dto.getPriority() != null ? dto.getPriority() : TaskPriority.MEDIA);

        return task;
    }

    public void updateEntityFromDTO(Task task, TaskCreateDTO dto) {
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());

        if (dto.getPriority() != null) {
            task.setPriority(dto.getPriority());
        }
    }
}