package com.cnu.spg.board.repository;

import com.cnu.spg.board.domain.Board;
import com.cnu.spg.board.domain.project.ProjectBoard;
import com.cnu.spg.board.domain.project.ProjectCategory;
import com.cnu.spg.board.dto.condition.BoardSearchCondition;
import com.cnu.spg.board.repository.project.ProjectCategoryRepository;
import com.cnu.spg.user.domain.User;
import com.cnu.spg.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DisplayName("Board db 처리 관련 테스트")
class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectCategoryRepository projectCategoryRepository;

    private static final String USERNAME_1 = "username1";
    private static final String USERNAME_2 = "username2";
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String TITLE_1_LIKE = "title_1_data";
    private static final String TITLE_2_LIKE = "title_2_data";

    private static final int NUMBER_OF_ELEMENTS = 29;
    private static final int NUMBER_OF_SOME = 10;
    private static final int NUMBER_OF_REST = 19;

    private static final String CONTENT_1_LIKE = "content1Like";
    private static final String CONTENT_2_LIKE = "content2Like";

    @BeforeEach
    void setUp() {
        String writer1Name = NAME_1;
        String wirter2Name = NAME_2;
        User user1 = User.createUser(writer1Name, USERNAME_1, "password");
        User user2 = User.createUser(wirter2Name, USERNAME_2, "password");

        userRepository.save(user1);
        userRepository.save(user2);

        ProjectCategory category1 = projectCategoryRepository.save(ProjectCategory.builder()
                .user(user1)
                .categoryName("category1")
                .build());
        ProjectCategory category2 = projectCategoryRepository.save(ProjectCategory.builder()
                .user(user1)
                .categoryName("category1")
                .build());

        for (int index = 0; index < NUMBER_OF_SOME; index++) {
            Board board = ProjectBoard.builder()
                    .title(TITLE_1_LIKE + index)
                    .content(CONTENT_1_LIKE + index)
                    .user(user1)
                    .projectCategory(category1)
                    .build();
            boardRepository.save(board);
        }

        for (int index = 0; index < NUMBER_OF_REST; index++) {
            Board board = ProjectBoard.builder()
                    .title(TITLE_2_LIKE + index)
                    .content(CONTENT_2_LIKE + index)
                    .user(user2)
                    .projectCategory(category2)
                    .build();
            boardRepository.save(board);
        }
    }

    @Test
    @DisplayName("게시판 옵션 없이 데이터 가져오기")
    void takeAllDataWithNoOpt() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        int firstPageNum = 0;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when

        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        assertEquals(NUMBER_OF_ELEMENTS, pageData.getTotalElements());
        int expectedPageNumber = NUMBER_OF_ELEMENTS / elementSize + ((NUMBER_OF_ELEMENTS % elementSize) > 0 ? 1 : 0);
        assertEquals(expectedPageNumber, pageData.getTotalPages());
        assertEquals(elementSize, pageData.getNumberOfElements());
    }

    @Test
    @DisplayName("page 2번째 데이터가 10인 것을 확인")
    void getAllDataWithPage2() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        int firstPageNum = 1;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when

        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        assertEquals(NUMBER_OF_ELEMENTS, pageData.getTotalElements());
        int expectedPageNumber = NUMBER_OF_ELEMENTS / elementSize + ((NUMBER_OF_ELEMENTS % elementSize) > 0 ? 1 : 0);
        assertEquals(expectedPageNumber, pageData.getTotalPages());
        assertEquals(elementSize, pageData.getNumberOfElements());
    }

    @Test
    @DisplayName("page 3번째 데이터가 9인 것을 확인")
    void getAllDataWithPage3() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        int firstPageNum = 2;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when

        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        assertEquals(NUMBER_OF_ELEMENTS, pageData.getTotalElements());
        int expectedPageNumber = NUMBER_OF_ELEMENTS / elementSize + ((NUMBER_OF_ELEMENTS % elementSize) > 0 ? 1 : 0);
        assertEquals(expectedPageNumber, pageData.getTotalPages());
        int expectedNowPageSize = 9;
        assertEquals(expectedNowPageSize, pageData.getNumberOfElements());
    }

    @Test
    @DisplayName("등록된 Board 가 없는 사용자 조회")
    void unMatchedUserBoards() throws Exception {
        // given
        String unContainsWriterName = "hello";

        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        boardSearchCondition.setWriterName(unContainsWriterName);

        int firstPageNum = 0;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when
        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        int expectedElementNumbers = 0;
        assertEquals(expectedElementNumbers, pageData.getTotalElements());
        int expectedPageNumber = 1;
        assertEquals(expectedPageNumber, pageData.getTotalPages());
        assertThat(pageData.getContent()).extracting("writerName")
                .doesNotContain(unContainsWriterName);
    }

    @Test
    @DisplayName("user1의 사용자 개수")
    void wirter1TakeData() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        boardSearchCondition.setWriterName(NAME_1);

        int firstPageNum = 0;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when
        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        assertEquals(NUMBER_OF_SOME, pageData.getTotalElements());
        int expectedPageNumber = NUMBER_OF_SOME / elementSize + ((NUMBER_OF_SOME % elementSize) > 0 ? 1 : 0);
        assertEquals(expectedPageNumber, pageData.getTotalPages());
        assertThat(pageData.getContent()).extracting("writerName")
                .contains(NAME_1);
    }

    @Test
    @DisplayName("content_data를 포함하는 board 테스트")
    void getDataFromContent() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        boardSearchCondition.setContentPart(CONTENT_2_LIKE);

        int firstPageNum = 0;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when
        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        assertEquals(NUMBER_OF_REST, pageData.getTotalElements());
        int expectedPageNumber = NUMBER_OF_REST / elementSize + ((NUMBER_OF_REST % elementSize) > 0 ? 1 : 0);
        assertEquals(expectedPageNumber, pageData.getTotalPages());
    }

    @Test
    @DisplayName("title1을 포함하는 content page 테스트")
    void getDataFromTitle() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        boardSearchCondition.setContentPart(CONTENT_2_LIKE);

        int firstPageNum = 0;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when
        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        assertEquals(NUMBER_OF_REST, pageData.getTotalElements());
        int expectedPageNumber = NUMBER_OF_REST / elementSize + ((NUMBER_OF_REST % elementSize) > 0 ? 1 : 0);
        assertEquals(expectedPageNumber, pageData.getTotalPages());
    }

    @Test
    @DisplayName("content2와 title2랑 공통으로 like 한 경우 찾기")
    void getDataFromContentAndTitle() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        boardSearchCondition.setTitlePart(TITLE_2_LIKE);
        boardSearchCondition.setContentPart(CONTENT_2_LIKE);

        int firstPageNum = 0;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when
        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        assertEquals(NUMBER_OF_REST, pageData.getTotalElements());
        int expectedPageNumber = NUMBER_OF_REST / elementSize + ((NUMBER_OF_REST % elementSize) > 0 ? 1 : 0);
        assertEquals(expectedPageNumber, pageData.getTotalPages());
    }

    @Test
    @DisplayName("content2와 title1랑 공통으로 like 한 경우 찾기")
    void getDataFromContentAndTitleWithEmpty() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        boardSearchCondition.setTitlePart(TITLE_1_LIKE);
        boardSearchCondition.setContentPart(CONTENT_2_LIKE);

        int firstPageNum = 0;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when
        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        int emptyElementNumber = 0;
        assertEquals(emptyElementNumber, pageData.getTotalElements());
        assertEquals(1, pageData.getTotalPages());
    }

    @Test
    @DisplayName("content1 과 wirter1을 사용하는 board 조회")
    void getDataFromContent1AndWriter1Name() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition();
        boardSearchCondition.setWriterName(NAME_1);
        boardSearchCondition.setContentPart(CONTENT_1_LIKE);

        int firstPageNum = 0;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when
        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        assertEquals(NUMBER_OF_SOME, pageData.getTotalElements());
        int expectedPageNumber = NUMBER_OF_SOME / elementSize + ((NUMBER_OF_SOME % elementSize) > 0 ? 1 : 0);
        assertEquals(expectedPageNumber, pageData.getTotalPages());
    }

    @Test
    @DisplayName("user1, content1, title1을 사용한 board 정보 조회")
    void getWriter1Content1Title1Board() throws Exception {
        // given
        BoardSearchCondition boardSearchCondition = new BoardSearchCondition(TITLE_1_LIKE, NAME_1, CONTENT_1_LIKE);

        int firstPageNum = 0;
        int elementSize = 10;
        Pageable pageableOpt = PageRequest.of(firstPageNum, elementSize);

        // when
        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageableOpt);
        Page<Board> pageData = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageableOpt);

        // then
        assertEquals(NUMBER_OF_SOME, pageData.getTotalElements());
        int expectedPageNumber = NUMBER_OF_SOME / elementSize + ((NUMBER_OF_SOME % elementSize) > 0 ? 1 : 0);
        assertEquals(expectedPageNumber, pageData.getTotalPages());
    }
}