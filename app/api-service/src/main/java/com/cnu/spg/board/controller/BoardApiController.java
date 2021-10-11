package com.cnu.spg.board.controller;

import com.cnu.spg.board.domain.BoardType;
import com.cnu.spg.board.dto.condition.BoardSearchCondition;
import com.cnu.spg.board.dto.condition.ProjectBoardCondition;
import com.cnu.spg.board.dto.request.BoardTypeRequset;
import com.cnu.spg.board.dto.request.BoardsRequset;
import com.cnu.spg.board.dto.request.ProjectCategoryRequestDto;
import com.cnu.spg.board.dto.response.BoardDetailResponse;
import com.cnu.spg.board.dto.BoardDto;
import com.cnu.spg.board.dto.response.CategoriesResponse;
import com.cnu.spg.board.exception.BoardTypeNotMatchException;
import com.cnu.spg.board.exception.NotExistBoardTypeException;
import com.cnu.spg.board.service.BoardAllService;
import com.cnu.spg.board.service.ProjectService;
import com.cnu.spg.config.resolver.UserId;
import com.cnu.spg.user.domain.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardAllService boardAllService;
    private final ProjectService projectService;

    @ApiOperation("[권한] 전체 게시판 정보를 제공")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "pageNum", value = "page number of pagination", required = true, paramType = "query"),
            @ApiImplicitParam(name = "elementSize", value = "each page element number", required = true, paramType = "query")
    })
    @GetMapping("/api/v1/boards")
    public ResponseEntity<Page<BoardDto>> getBoards(@Valid BoardsRequset boardsRequset,
                                                    @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 20) Pageable pageable) {
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition(boardsRequset.getPartTitle(), boardsRequset.getWriterName(), boardsRequset.getPartOfContent());

        return ResponseEntity.ok().body(boardAllService.findBoardsOnePage(boardSearchCondition, pageable));
    }

    @ApiOperation("[권한] board type에 따른 정보 조회")
    @GetMapping("/api/v1/boards/{boardType}")
    public ResponseEntity<Page<BoardDto>> findBoardByType(@PathVariable String boardType, @Valid BoardTypeRequset boardsRequset,
                                                          @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 20) Pageable pageable) {
        BoardType boardTypeEnum = BoardType.findBoardTypeByKey(boardType)
                .orElseThrow(NotExistBoardTypeException::new);

        ProjectBoardCondition projectBoardCondition = new ProjectBoardCondition(boardsRequset.getPartOfContent(), boardsRequset.getWriterName(), boardsRequset.getPartOfContent());
        if (boardTypeEnum == BoardType.PROJECT) {
            return ResponseEntity.ok().body(projectService.findProjectBoardsOnePage(projectBoardCondition, pageable, boardsRequset.getCategoryId()));
        }

        throw new BoardTypeNotMatchException();
    }


    @ApiOperation("[권한] board 정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "board id 정보", readOnly = true, paramType = "path")
    })
    @GetMapping("/api/v1/boards/{boardType}/{id}")
    public ResponseEntity<? extends BoardDetailResponse> getBoard(@PathVariable String boardType, @PathVariable("id") Long boardId) {
        BoardType boardTypeEnum = BoardType.findBoardTypeByKey(boardType)
                .orElseThrow(NotExistBoardTypeException::new);
        return ResponseEntity.ok().body(boardAllService.getBoard(boardTypeEnum, boardId));
    }

    @ApiOperation("[권한] project board를 위한 category 정보 가져오기")
    @GetMapping("/api/v1/boards/categories")
    public ResponseEntity<CategoriesResponse> getAllJoinedCategories(@UserId User user) {
        return ResponseEntity.ok(projectService.findAllUserCategories(user.getId()));
    }

    @ApiOperation("[권한] project category 추가")
    @PostMapping("/api/v1/boards/categories")
    public ResponseEntity<URI> createCategory(@UserId User user, @Valid @RequestBody ProjectCategoryRequestDto projectCategoryRequestDto) {
        Long savedId = projectService.createProjectCategory(user, projectCategoryRequestDto.getCategoryName(), projectCategoryRequestDto.getParentCategoryId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(savedId).toUri();

        return ResponseEntity.created(uri).build();
    }
}
