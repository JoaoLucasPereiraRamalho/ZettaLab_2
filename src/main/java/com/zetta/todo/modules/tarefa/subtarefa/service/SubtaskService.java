package com.zetta.todo.modules.tarefa.subtarefa.service;

import com.zetta.todo.common.exception.BusinessException; // IMPORTANTE
import com.zetta.todo.modules.tarefa.subtarefa.domain.Subtask;
import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskResponseDTO;
import com.zetta.todo.modules.tarefa.subtarefa.repository.SubtaskRepository;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
import com.zetta.todo.modules.tarefa.tarefa.repository.TaskRepository;
import com.zetta.todo.modules.usuario.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public SubtaskResponseDTO create(Long taskId, String description) {
        User user = getLoggedUser();
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("task.not.found", taskId));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new BusinessException("task.owner.error");
        }

        Subtask subtask = new Subtask();
        subtask.setDescription(description);
        subtask.setTask(task);
        subtask.setStatus(TaskStatus.PENDING);

        subtask = subtaskRepository.save(subtask);
        return toResponseDTO(subtask);
    }

    public void delete(Long id) {
        User user = getLoggedUser();
        Subtask subtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("subtask.not.found", id));

        if (!subtask.getTask().getUser().getId().equals(user.getId())) {
            throw new BusinessException("subtask.owner.error");
        }

        subtaskRepository.delete(subtask);
    }

    public SubtaskResponseDTO updateStatus(Long id, TaskStatus status) {
        User user = getLoggedUser();
        Subtask subtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("subtask.not.found", id));

        if (!subtask.getTask().getUser().getId().equals(user.getId())) {
            throw new BusinessException("subtask.owner.error");
        }

        subtask.setStatus(status);
        subtask = subtaskRepository.save(subtask);
        return toResponseDTO(subtask);
    }

    private User getLoggedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private SubtaskResponseDTO toResponseDTO(Subtask subtask) {
        SubtaskResponseDTO dto = new SubtaskResponseDTO();
        dto.setId(subtask.getId());
        dto.setDescription(subtask.getDescription());
        dto.setStatus(subtask.getStatus());
        dto.setTaskId(subtask.getTask().getId());
        return dto;
    }
}