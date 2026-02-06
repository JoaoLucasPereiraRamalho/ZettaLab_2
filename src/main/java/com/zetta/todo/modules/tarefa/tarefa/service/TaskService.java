package com.zetta.todo.modules.tarefa.tarefa.service;

import com.zetta.todo.common.exception.BusinessException;
import com.zetta.todo.modules.tarefa.categoria.repository.CategoryRepository;
import com.zetta.todo.modules.tarefa.tarefa.domain.Task;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskStatus;
import com.zetta.todo.modules.tarefa.tarefa.dto.DashboardResponseDTO;
import com.zetta.todo.modules.tarefa.tarefa.dto.TaskCreateDTO;
import com.zetta.todo.modules.tarefa.tarefa.dto.TaskResponseDTO;
import com.zetta.todo.modules.tarefa.tarefa.mapper.TaskMapper;
import com.zetta.todo.modules.tarefa.tarefa.repository.TaskRepository;
import com.zetta.todo.modules.usuario.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO create(TaskCreateDTO dto) {
        User user = getLoggedUser();

        var category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new BusinessException("category.not.found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new BusinessException("category.owner.error");
        }

        if (dto.getDueDate() != null && dto.getDueDate().isBefore(java.time.LocalDate.now())) {
            throw new BusinessException("A data de vencimento não pode ser no passado.");
        }

        Task task = taskMapper.toEntity(dto);

        task.setUser(user);
        task.setCategory(category);

        task = taskRepository.save(task);
        return taskMapper.toResponseDTO(task);
    }

    public List<TaskResponseDTO> listAll() {
        User user = getLoggedUser();
        return taskRepository.findAllByUserId(user.getId()).stream()
                .map(taskMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO findById(Long id) {
        Task task = validateOwnerAndGetTask(id);
        return taskMapper.toResponseDTO(task);
    }

    public TaskResponseDTO update(Long id, TaskCreateDTO dto) {
        Task task = validateOwnerAndGetTask(id);

        if (dto.getDueDate() != null && dto.getDueDate().isBefore(java.time.LocalDate.now())) {
            throw new BusinessException("A data de vencimento não pode ser no passado.");
        }

        if (!task.getCategory().getId().equals(dto.getCategoryId())) {
            var newCategory = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new BusinessException("category.not.found"));

            if (!newCategory.getUser().getId().equals(task.getUser().getId())) {
                throw new BusinessException("task.category.invalid");
            }
            task.setCategory(newCategory);
        }

        taskMapper.updateEntityFromDTO(task, dto);

        task = taskRepository.save(task);
        return taskMapper.toResponseDTO(task);
    }

    public TaskResponseDTO updateStatus(Long id, TaskStatus status) {
        Task task = validateOwnerAndGetTask(id);

        validateSubtaskConsistency(task, status);

        task.setStatus(status);
        taskRepository.save(task);
        return taskMapper.toResponseDTO(task);
    }

    public void delete(Long id) {
        Task task = validateOwnerAndGetTask(id);
        taskRepository.delete(task);
    }

    public List<DashboardResponseDTO> listByDashboard() {
        User user = getLoggedUser();

        var allTasks = taskRepository.findAllByUserId(user.getId());
        var allCategories = categoryRepository.findAllByUserId(user.getId());

        List<DashboardResponseDTO> dashboard = new ArrayList<>();

        for (var category : allCategories) {
            var tasksOfCategory = allTasks.stream()
                    .filter(task -> task.getCategory().getId().equals(category.getId()))
                    .map(taskMapper::toResponseDTO) // Uso do Mapper
                    .collect(Collectors.toList());

            dashboard.add(new DashboardResponseDTO(
                    category.getId(),
                    category.getName(),
                    category.getColor(),
                    tasksOfCategory));
        }
        return dashboard;
    }

    // --- MÉTODOS AUXILIARES ---

    private void validateSubtaskConsistency(Task task, TaskStatus newStatus) {
        if (newStatus == TaskStatus.COMPLETED) {
            if (task.getSubtasks() != null && !task.getSubtasks().isEmpty()) {
                boolean hasPending = task.getSubtasks().stream()
                        .anyMatch(sub -> sub.getStatus() != TaskStatus.COMPLETED);

                if (hasPending) {
                    throw new BusinessException("Não é possível concluir a tarefa pois existem subtarefas pendentes.");
                }
            }
        }
    }

    private Task validateOwnerAndGetTask(Long id) {
        User user = getLoggedUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("task.not.found", id));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new BusinessException("task.owner.error");
        }
        return task;
    }

    private User getLoggedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}