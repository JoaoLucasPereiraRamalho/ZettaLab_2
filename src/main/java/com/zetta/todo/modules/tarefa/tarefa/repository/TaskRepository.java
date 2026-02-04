package com.zetta.todo.modules.tarefa.tarefa.repository;

import com.zetta.todo.modules.tarefa.tarefa.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUserId(Long userId);
}