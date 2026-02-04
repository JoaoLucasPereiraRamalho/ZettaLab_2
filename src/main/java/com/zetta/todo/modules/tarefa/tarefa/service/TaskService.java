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
import com.zetta.todo.modules.tarefa.subtarefa.dto.SubtaskResponseDTO;
import com.zetta.todo.modules.tarefa.subtarefa.domain.Subtask;
import com.zetta.todo.modules.tarefa.tarefa.dto.DashboardResponseDTO;
import com.zetta.todo.modules.tarefa.categoria.domain.Category;
import com.zetta.todo.modules.tarefa.tarefa.domain.TaskPriority;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.ArrayList;

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

        if (dto.getPriority() != null) {
            task.setPriority(dto.getPriority());
        } else {
            task.setPriority(TaskPriority.MEDIA);
        }

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
        dto.setPriority(task.getPriority());

        // CONVERTER SUBTAREFAS
        if (task.getSubtasks() != null) {
            List<SubtaskResponseDTO> subItems = task.getSubtasks().stream()
                    .map(sub -> {
                        SubtaskResponseDTO subDto = new SubtaskResponseDTO();
                        subDto.setId(sub.getId());
                        subDto.setDescription(sub.getDescription());
                        subDto.setStatus(sub.getStatus());
                        subDto.setTaskId(task.getId());
                        return subDto;
                    })
                    .collect(Collectors.toList());
            dto.setSubtasks(subItems);
        } else {
            dto.setSubtasks(Collections.emptyList());
        }

        return dto;
    }

    public List<DashboardResponseDTO> listByDashboard() {
        User user = getLoggedUser();

        List<Task> allTasks = taskRepository.findAllByUserId(user.getId());

        List<Category> allCategories = categoryRepository.findAllByUserId(user.getId());

        List<DashboardResponseDTO> dashboard = new ArrayList<>();

        for (Category category : allCategories) {
            var tasksOfCategory = allTasks.stream()
                    .filter(task -> task.getCategory().getId().equals(category.getId()))
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());

            dashboard.add(new DashboardResponseDTO(
                    category.getId(),
                    category.getName(),
                    category.getColor(),
                    tasksOfCategory));
        }

        return dashboard;
    }

    public TaskResponseDTO findById(Long id) {
        Task task = validateOwnerAndGetTask(id);
        return toResponseDTO(task);
    }

    public TaskResponseDTO update(Long id, TaskCreateDTO dto) {
        Task task = validateOwnerAndGetTask(id);

        if (!task.getCategory().getId().equals(dto.getCategoryId())) {
            var newCategory = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Nova categoria não encontrada"));

            if (!newCategory.getUser().getId().equals(task.getUser().getId())) {
                throw new RuntimeException("Categoria inválida");
            }
            task.setCategory(newCategory);
        }

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        if (dto.getPriority() != null)
            task.setPriority(dto.getPriority());

        task = taskRepository.save(task);
        return toResponseDTO(task);
    }

    public TaskResponseDTO updateStatus(Long id, TaskStatus status) {
        Task task = validateOwnerAndGetTask(id);
        task.setStatus(status);
        taskRepository.save(task);
        return toResponseDTO(task);
    }

    public void delete(Long id) {
        Task task = validateOwnerAndGetTask(id);
        taskRepository.delete(task);
    }

    private Task validateOwnerAndGetTask(Long id) {
        User user = getLoggedUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para acessar esta tarefa");
        }
        return task;
    }
}