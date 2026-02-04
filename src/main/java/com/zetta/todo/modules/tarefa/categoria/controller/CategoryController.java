package com.zetta.todo.modules.tarefa.categoria.controller;

import com.zetta.todo.modules.tarefa.categoria.dto.CategoryCreateDTO;
import com.zetta.todo.modules.tarefa.categoria.dto.CategoryResponseDTO;
import com.zetta.todo.modules.tarefa.categoria.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CategoryCreateDTO dto) {
        var category = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> list() {
        var categories = categoryService.listAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        var category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id,
            @RequestBody @Valid CategoryCreateDTO dto) {
        var category = categoryService.update(id, dto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}