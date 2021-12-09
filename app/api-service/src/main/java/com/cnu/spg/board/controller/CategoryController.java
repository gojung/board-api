package com.cnu.spg.board.controller;

import com.cnu.spg.board.dto.request.ProjectCategoryRequest;
import com.cnu.spg.board.dto.response.CategoriesResponse;
import com.cnu.spg.board.service.ProjectService;
import com.cnu.spg.config.resolver.UserId;
import com.cnu.spg.user.domain.User;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final ProjectService projectService;

    @ApiOperation("[권한] project board를 위한 category 정보 가져오기")
    @GetMapping("/api/v1/boards/project/categories")
    public ResponseEntity<CategoriesResponse> findAllJoinedCategories(@UserId User user) {
        return ResponseEntity.ok(projectService.findAllUserCategories(user.getId()));
    }

    @ApiOperation("[권한] project category 추가")
    @PostMapping("/api/v1/boards/project/categories")
    public ResponseEntity<URI> createCategory(@UserId User user, @Valid @RequestBody ProjectCategoryRequest projectCategoryRequest) {
        Long savedId = projectService.createProjectCategory(user, projectCategoryRequest.getCategoryName(), projectCategoryRequest.getParentCategoryId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(savedId).toUri();

        return ResponseEntity.created(uri).build();
    }
}
