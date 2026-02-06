package com.zetta.todo.modules.tarefa.categoria.mapper;

import com.zetta.todo.modules.tarefa.categoria.domain.Category;
import com.zetta.todo.modules.tarefa.categoria.dto.CategoryCreateDTO;
import com.zetta.todo.modules.tarefa.categoria.dto.CategoryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponseDTO toResponseDTO(Category category) {

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getColor());
    }

    public Category toEntity(CategoryCreateDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setColor(dto.getColor());
        return category;
    }

    public void updateEntityFromDTO(Category category, CategoryCreateDTO dto) {
        category.setName(dto.getName());
        category.setColor(dto.getColor());
    }
}
