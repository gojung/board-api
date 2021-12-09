package com.cnu.spg.board.service;

import com.cnu.spg.board.domain.Board;
import com.cnu.spg.board.domain.BoardType;
import com.cnu.spg.board.domain.project.ProjectBoard;
import com.cnu.spg.board.domain.project.ProjectReference;
import com.cnu.spg.board.dto.BoardCommentDto;
import com.cnu.spg.board.dto.BoardDto;
import com.cnu.spg.board.dto.ProjectUserReferenceDto;
import com.cnu.spg.board.dto.condition.BoardSearchCondition;
import com.cnu.spg.board.dto.projection.BoardCommentCountProjection;
import com.cnu.spg.board.dto.response.BoardDetailResponse;
import com.cnu.spg.board.dto.response.ProjectBoardResponse;
import com.cnu.spg.board.exception.BoardNotFoundException;
import com.cnu.spg.board.exception.BoardTypeNotMatchException;
import com.cnu.spg.board.repository.BoardRepository;
import com.cnu.spg.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardAllService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public Page<BoardDto> findBoardsOnePage(BoardSearchCondition boardSearchCondition, Pageable pageable) {
        List<Long> ids = boardRepository.findIdsFromPaginationWithKeyword(boardSearchCondition, pageable);
        List<BoardCommentCountProjection> countListAndBoardIdBulk = commentRepository.findCountListAndBoardIdBulk(ids);

        Map<Long, BoardCommentCountProjection> boardIdWithCommentNumber = countListAndBoardIdBulk
                .stream()
                .collect(Collectors.toMap(BoardCommentCountProjection::getId, Function.identity()));

        Page<Board> pageDataFromBoardByIds = boardRepository.findPageDataFromBoardByIds(ids, boardSearchCondition, pageable);

        return pageDataFromBoardByIds.map(board -> BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writerId(board.getWriterId())
                .writerName(board.getWriterName())
                .commentCount(commentCountFromCommentDto(boardIdWithCommentNumber.get(board.getId())))
                .build());
    }

    private long commentCountFromCommentDto(BoardCommentCountProjection boardCommentCountProjection) {
        if (boardCommentCountProjection == null) return 0L;

        return boardCommentCountProjection.getNumberOfComments();
    }

    public BoardDetailResponse getBoard(BoardType boardType, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        List<BoardCommentDto> comments = board.getComments()
                .stream()
                .map(comment -> new BoardCommentDto(comment.getId(), comment.getWriterName(), comment.getContent()))
                .collect(Collectors.toList()); // N + 1 발생

        if (boardType == BoardType.PROJECT && board instanceof ProjectBoard) {
            return findProjectBoardElement((ProjectBoard) board, comments);
        }

        throw new BoardTypeNotMatchException();
    }

    private ProjectBoardResponse findProjectBoardElement(ProjectBoard projectBoard, List<BoardCommentDto> comments) {
        List<Long> referenceIds = projectBoard.getProjectReference().stream()
                .map(projectReference -> projectBoard.getId())
                .collect(Collectors.toList());
        List<ProjectReference> referencedUsersByIds = boardRepository.findReferencedUsersByIds(referenceIds);
        List<ProjectUserReferenceDto> referenceUserDtos = referencedUsersByIds.stream()
                .map(projectReference -> new ProjectUserReferenceDto(projectReference.getReferenceUser().getName()))
                .collect(Collectors.toList());

        return new ProjectBoardResponse(projectBoard, comments, referenceUserDtos);
    }
}
