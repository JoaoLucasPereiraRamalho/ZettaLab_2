package com.zetta.todo.modules.tarefa.categoria.service;

import com.zetta.todo.modules.tarefa.categoria.domain.Category;
import com.zetta.todo.modules.tarefa.categoria.dto.CategoryCreateDTO;
import com.zetta.todo.modules.tarefa.categoria.dto.CategoryResponseDTO;
import com.zetta.todo.modules.tarefa.categoria.repository.CategoryRepository;
import com.zetta.todo.modules.usuario.domain.User;
import com.zetta.todo.modules.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResponseDTO create(CategoryCreateDTO dto) {
        User user = getLoggedUser(); // Descobre quem está logado

        Category category = new Category();
        category.setName(dto.getName());
        category.setColor(dto.getColor());
        category.setUser(user);

        category = categoryRepository.save(category);
        return toResponseDTO(category);
    }

    public List<CategoryResponseDTO> listAll() {
        User user = getLoggedUser();
        var categories = categoryRepository.findAllByUserId(user.getId());

        return categories.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private User getLoggedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private CategoryResponseDTO toResponseDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setColor(category.getColor());
        return dto;
    }

    public CategoryResponseDTO findById(Long id) {
        User user = getLoggedUser();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado a esta categoria");
        }
        return toResponseDTO(category);
    }

    public CategoryResponseDTO update(Long id, CategoryCreateDTO dto) {
        User user = getLoggedUser();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não pode alterar esta categoria");
        }

        category.setName(dto.getName());
        category.setColor(dto.getColor());

        category = categoryRepository.save(category);
        return toResponseDTO(category);
    }

    public void delete(Long id) {
        User user = getLoggedUser();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não pode deletar esta categoria");
        }

        categoryRepository.delete(category);
    }
}
