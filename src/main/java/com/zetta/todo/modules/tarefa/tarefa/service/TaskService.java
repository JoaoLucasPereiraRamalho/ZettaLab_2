package com.zetta.todo.modules.tarefa.tarefa.service;

import com.zetta.todo.modules.tarefa.tarefa.domain.Task;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
import com.zetta.todo.modules.tarefa.categoria.dto.CategoryResponseDTO;
import com.zetta.todo.modules.tarefa.tarefa.dto.TaskCreateDTO;
import com.zetta.todo.modules.tarefa.tarefa.dto.TaskResponseDTO;
import com.zetta.todo.modules.tarefa.categoria.repository.CategoryRepository;
import com.zetta.todo.modules.tarefa.tarefa.repository.TaskRepository;
import com.zetta.todo.modules.usuario.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskResponseDTO create(TaskCreateDTO dto) {
        User user = getLoggedUser();

        var category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Essa categoria não pertence a você!");
        }

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setStatus(TaskStatus.PENDING);
        task.setUser(user);
        task.setCategory(category);

        task = taskRepository.save(task);

        return toResponseDTO(task);
    }

    public List<TaskResponseDTO> listAll() {
        User user = getLoggedUser();
        var tasks = taskRepository.findAllByUserId(user.getId());

        return tasks.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private User getLoggedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private TaskResponseDTO toResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());

        CategoryResponseDTO catDto = new CategoryResponseDTO();
        catDto.setId(task.getCategory().getId());
        catDto.setName(task.getCategory().getName());
        catDto.setColor(task.getCategory().getColor());
        dto.setCategory(catDto);

        return dto;
    }
}