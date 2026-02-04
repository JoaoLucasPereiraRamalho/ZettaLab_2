package com.zetta.todo.modules.tarefa.subtarefa.service;

import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
import com.zetta.todo.modules.tarefa.subtarefa.domain.Subtask;
import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskCreateDTO;
import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskResponseDTO;
import com.zetta.todo.modules.tarefa.subtarefa.repository.SubtaskRepository;
import com.zetta.todo.modules.tarefa.tarefa.repository.TaskRepository;
import com.zetta.todo.modules.usuario.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public SubtaskResponseDTO create(SubtaskCreateDTO dto) {
        User user = getLoggedUser();

        var task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não pode adicionar itens na tarefa de outro usuário");
        }

        Subtask subtask = new Subtask();
        subtask.setDescription(dto.getDescription());
        subtask.setTask(task);
        subtask.setStatus(TaskStatus.PENDING);

        subtask = subtaskRepository.save(subtask);

        return toResponseDTO(subtask);
    }

    public List<SubtaskResponseDTO> listByTask(Long taskId) {
        return subtaskRepository.findAllByTaskId(taskId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
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

    public void delete(Long id) {
        User user = getLoggedUser();
        Subtask subtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (!subtask.getTask().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para remover este item");
        }

        subtaskRepository.delete(subtask);
    }

    public SubtaskResponseDTO updateStatus(Long id, TaskStatus status) {
        User user = getLoggedUser();
        Subtask subtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (!subtask.getTask().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Permissão negada");
        }

        subtask.setStatus(status);
        subtask = subtaskRepository.save(subtask);
        return toResponseDTO(subtask);
    }
}