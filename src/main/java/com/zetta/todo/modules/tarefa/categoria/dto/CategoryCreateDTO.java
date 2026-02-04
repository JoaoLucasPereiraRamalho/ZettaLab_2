package com.zetta.todo.modules.tarefa.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateDTO {
    @NotBlank(message = "{validation.name.required}")
    @Size(min = 3, max = 50, message = "{validation.name.size}")
    private String name;

    @NotBlank(message = "{validation.color.required}")
    private String color;
}