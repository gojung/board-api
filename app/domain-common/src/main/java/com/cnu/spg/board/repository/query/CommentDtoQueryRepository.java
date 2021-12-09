package com.cnu.spg.board.repository.query;

import com.cnu.spg.board.dto.projection.BoardCommentCountProjection;

import java.util.List;

public interface CommentDtoQueryRepository {
    List<BoardCommentCountProjection> findCountListAndBoardIdBulk(List<Long> boardIdx);
}
