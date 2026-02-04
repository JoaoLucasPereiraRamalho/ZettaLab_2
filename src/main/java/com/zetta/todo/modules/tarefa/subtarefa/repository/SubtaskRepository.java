package com.zetta.todo.modules.tarefa.subtarefa.repository;

import com.zetta.todo.modules.tarefa.subtarefa.domain.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    // Busca os itens de checklist de uma tarefa específica
    List<Subtask> findAllByTaskId(Long taskId);
}