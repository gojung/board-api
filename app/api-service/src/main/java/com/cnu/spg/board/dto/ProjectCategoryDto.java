package com.cnu.spg.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectCategoryDto {
    private Long categoryId;

    private String categoryName;

    private List<ProjectCategoryDto> children;
}
