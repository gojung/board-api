package com.cnu.spg.board.dto.response;

import com.cnu.spg.board.dto.ProjectCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoriesResponse {
    private List<ProjectCategoryDto> projectCategories;
}
