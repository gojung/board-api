package com.cnu.spg.board.service;

import com.cnu.spg.board.domain.Board;
import com.cnu.spg.board.domain.project.ProjectBoard;
import com.cnu.spg.board.domain.project.ProjectCategory;
import com.cnu.spg.board.dto.condition.BoardSearchCondition;
import com.cnu.spg.board.dto.response.BoardResponse;
import com.cnu.spg.board.repository.BoardRepository;
import com.cnu.spg.board.repository.project.ProjectCategoryRepository;
import com.cnu.spg.user.domain.User;
import com.cnu.spg.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

//import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BoardAllServiceTest {
    @Autowired
    BoardAllService boardAllService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    ProjectCategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;

    void saveProject(int number, User user, ProjectCategory category) {
        userRepository.save(user);
        categoryRepository.save(category);
        for (int index = 0; index < number; index++) {
            Board board = ProjectBoard.builder()
                    .projectCategory(category)
                    .content("content_" + index)
                    .title("title_" + index)
                    .user(user)
                    .build();

            boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("한 page Board 정보 조회")
    void findBoardsOnePageTest() {
        // given
        User user = User.createUser("name", "john@gmail.com", "Abc123!@");
        ProjectCategory projectCategory = ProjectCategory.builder()
                .user(user)
                .categoryName("category")
                .build();
        saveProject(21, user, projectCategory);

        // when
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id").descending());
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition(null, null, null);
        Page<BoardResponse> boardsOnePage = boardAllService.findBoardsOnePage(boardSearchCondition, pageable);

        // then
        assertEquals(20, boardsOnePage.getContent().size());
        assertEquals(21, boardsOnePage.getTotalElements());
        assertEquals(2, boardsOnePage.getTotalPages());
    }

    @Test
    @DisplayName("한페이지 보다 적은 경우")
    void findOnePageLessOnePageTest() throws Exception {
        // given
        User user = User.createUser("name", "john@gmail.com", "Abc123!@");
        ProjectCategory projectCategory = ProjectCategory.builder()
                .user(user)
                .categoryName("category")
                .build();
        saveProject(11, user, projectCategory);

        // when
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id").descending());
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition(null, null, null);
        Page<BoardResponse> boardsOnePage = boardAllService.findBoardsOnePage(boardSearchCondition, pageable);

        // then
        assertEquals(11, boardsOnePage.getContent().size());
        assertEquals(11, boardsOnePage.getTotalElements());
        assertEquals(1, boardsOnePage.getTotalPages());
    }

    @Test
    @DisplayName("아무 것도 없는 경우")
    void findOnePageWithEmptyTest() throws Exception {
        // given

        // when
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id").descending());
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition(null, null, null);
        Page<BoardResponse> boardsOnePage = boardAllService.findBoardsOnePage(boardSearchCondition, pageable);

        // then
        assertEquals(0, boardsOnePage.getContent().size());
        assertEquals(0, boardsOnePage.getTotalElements());
        assertEquals(1, boardsOnePage.getTotalPages());
    }

    @Test
    @DisplayName("board 정보 조회")
    void getOneBoardTest() {
        // given


        // when

        // then

    }
}