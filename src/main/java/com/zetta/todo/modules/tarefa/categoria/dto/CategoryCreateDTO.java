package com.zetta.todo.modules.tarefa.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CategoryCreateDTO {
    @NotBlank(message = "O nome da categoria é obrigatório")
    private String name;

    @NotBlank(message = "A cor é obrigatória")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "A cor deve ser um código Hex válido (ex: #FF0000)")
    private String color;
}