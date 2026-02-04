package com.zetta.todo.modules.tarefa.tarefa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class DashboardResponseDTO {
    private Long categoryId;
    private String categoryName;
    private String categoryColor;
    private List<TaskResponseDTO> tasks;
}