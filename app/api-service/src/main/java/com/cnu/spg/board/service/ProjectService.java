package com.cnu.spg.board.service;

import com.cnu.spg.board.domain.Board;
import com.cnu.spg.board.domain.project.ProjectCategory;
import com.cnu.spg.board.dto.BoardDto;
import com.cnu.spg.board.dto.ProjectCategoryDto;
import com.cnu.spg.board.dto.condition.ProjectBoardCondition;
import com.cnu.spg.board.dto.projection.BoardCommentCountProjection;
import com.cnu.spg.board.dto.response.CategoriesResponse;
import com.cnu.spg.board.exception.CategoryNotFoundException;
import com.cnu.spg.board.repository.BoardRepository;
import com.cnu.spg.board.repository.CommentRepository;
import com.cnu.spg.board.repository.project.ProjectCategoryRepository;
import com.cnu.spg.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectCategoryRepository projectCategoryRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public CategoriesResponse findAllUserCategories(Long userId) {
        List<ProjectCategory> categories = projectCategoryRepository.findCategoriesByUserId(userId);
        List<ProjectCategory> parentsCategories = categories
                .parallelStream()
                .filter(projectCategory -> projectCategory.getParent() == null)
                .collect(Collectors.toList());

        Map<Long, List<ProjectCategory>> parentsMappedCategoriesCache = categories
                .stream()
                .filter(projectCategory -> projectCategory.getParent() != null)
                .collect(groupingBy(projectCategory -> projectCategory.getParent().getId()));

        return new CategoriesResponse(createTreeCategories(parentsCategories, parentsMappedCategoriesCache));
    }

    private List<ProjectCategoryDto> createTreeCategories(List<ProjectCategory> parentsCategories, Map<Long, List<ProjectCategory>> parentsMappedCategoriesCache) {
        Queue<ProjectCategory> projectCategoryQueue = new LinkedList<>();
        List<ProjectCategoryDto> projectCategoryDtos = new ArrayList<>();
        Map<Long, ProjectCategoryDto> parentCache = new HashMap<>();

        for (ProjectCategory parent : parentsCategories) {
            ProjectCategoryDto projectCategoryDto = new ProjectCategoryDto(parent.getId(), parent.getCategoryName(), new ArrayList<>());
            projectCategoryQueue.add(parent);
            projectCategoryDtos.add(projectCategoryDto);
            parentCache.put(projectCategoryDto.getCategoryId(), projectCategoryDto);
        }

        while (!projectCategoryQueue.isEmpty()) {
            ProjectCategory parent = projectCategoryQueue.poll();
            ProjectCategoryDto parentDto = parentCache.get(parent.getId());
            List<ProjectCategory> children = parentsMappedCategoriesCache.getOrDefault(parent.getId(), new ArrayList<>());

            for (ProjectCategory child : children) {
                ProjectCategoryDto projectCategoryDto = new ProjectCategoryDto(child.getId(), child.getCategoryName(), new ArrayList<>());
                projectCategoryQueue.add(child);
                parentDto.getChildren().add(projectCategoryDto);
                parentCache.put(projectCategoryDto.getCategoryId(), projectCategoryDto);
            }
        }

        return projectCategoryDtos;
    }

    @Transactional
    public Long createProjectCategory(User user, String categoryName, Long parentCategoryId) {
        ProjectCategory parentCategory = null;
        if (parentCategoryId != null) {
            parentCategory = projectCategoryRepository.findById(parentCategoryId)
                    .orElseThrow(CategoryNotFoundException::new);
        }

        ProjectCategory createdCategory = ProjectCategory.builder()
                .categoryName(categoryName)
                .parent(parentCategory)
                .user(user)
                .build();

        ProjectCategory savedCategory = projectCategoryRepository.save(createdCategory);

        return savedCategory.getId();
    }

    public Page<BoardDto> findProjectBoardsOnePage(ProjectBoardCondition projectBoardCondition, Pageable pageable, Long categoryId) {
        ProjectCategory category = projectCategoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
        List<Long> ids = boardRepository.findProjectBoardIdsFromPaginationWithKeyword(projectBoardCondition, category, pageable);
        List<BoardCommentCountProjection> countListAndBoardIdBulk = commentRepository.findCountListAndBoardIdBulk(ids);

        Map<Long, BoardCommentCountProjection> boardIdWithCommentNumber = countListAndBoardIdBulk
                .stream()
                .collect(Collectors.toMap(BoardCommentCountProjection::getId, Function.identity()));

        Page<Board> pageDataFromBoardByIds = boardRepository.findProjectPageDataFromBoardByIds(ids, projectBoardCondition, category, pageable);

        List<BoardDto> boardRespons = pageDataFromBoardByIds
                .getContent().stream()
                .map(board -> BoardDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .writerId(board.getWriterId())
                        .writerName(board.getWriterName())
                        .commentCount(commentCountFromCommentDto(boardIdWithCommentNumber.get(board.getId())))
                        .build()
                ).collect(Collectors.toList());

        return new PageImpl<>(boardRespons, pageable, pageDataFromBoardByIds.getTotalElements());
    }

    private long commentCountFromCommentDto(BoardCommentCountProjection boardCommentCountProjection) {
        if (boardCommentCountProjection == null) return 0L;

        return boardCommentCountProjection.getNumberOfComments();
    }
}
