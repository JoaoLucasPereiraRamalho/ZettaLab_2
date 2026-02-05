package com.zetta.todo.modules.tarefa.categoria.repository;

import com.zetta.todo.modules.tarefa.categoria.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserId(Long userId);

    boolean existsByNameIgnoreCaseAndUserId(String name, Long userId);
}