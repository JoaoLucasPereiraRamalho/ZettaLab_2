package com.zetta.todo.modules.tarefa.categoria.service;

import com.zetta.todo.common.exception.BusinessException;
import com.zetta.todo.modules.tarefa.categoria.domain.Category;
import com.zetta.todo.modules.tarefa.categoria.dto.CategoryCreateDTO;
import com.zetta.todo.modules.tarefa.categoria.dto.CategoryResponseDTO;
import com.zetta.todo.modules.tarefa.categoria.repository.CategoryRepository;
import com.zetta.todo.modules.usuario.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO create(CategoryCreateDTO dto) {
        User user = getLoggedUser();

        if (categoryRepository.existsByNameIgnoreCaseAndUserId(dto.getName(), user.getId())) {
            throw new BusinessException("Você já possui uma categoria com este nome.");
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setColor(dto.getColor());
        category.setUser(user);

        category = categoryRepository.save(category);
        return toResponseDTO(category);
    }

    public List<CategoryResponseDTO> listAll() {
        User user = getLoggedUser();
        return categoryRepository.findAllByUserId(user.getId()).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO findById(Long id) {
        User user = getLoggedUser();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("category.not.found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new BusinessException("category.owner.error");
        }
        return toResponseDTO(category);
    }

    public CategoryResponseDTO update(Long id, CategoryCreateDTO dto) {
        User user = getLoggedUser();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("category.not.found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new BusinessException("category.owner.error");
        }

        category.setName(dto.getName());
        category.setColor(dto.getColor());

        category = categoryRepository.save(category);
        return toResponseDTO(category);
    }

    public void delete(Long id) {
        User user = getLoggedUser();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("category.not.found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new BusinessException("category.delete.error");
        }

        categoryRepository.delete(category);
    }

    private User getLoggedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName(), category.getColor());
    }
}