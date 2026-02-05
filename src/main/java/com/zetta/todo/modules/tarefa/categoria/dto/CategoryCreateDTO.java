package com.zetta.todo.modules.tarefa.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateDTO {
    @NotBlank(message = "{validation.name.required}")
    @Size(min = 3, max = 50, message = "{validation.name.size}")
    private String name;

    @NotBlank(message = "{validation.color.required}")
    private String color;
}