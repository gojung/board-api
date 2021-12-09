package com.cnu.spg.board.dto.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ProjectCategoryRequest {
    @Size(min = 1, max = 75)
    private String categoryName;
    private Long parentCategoryId;
}
