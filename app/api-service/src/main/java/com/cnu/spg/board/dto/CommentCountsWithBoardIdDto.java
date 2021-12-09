package com.cnu.spg.board.dto;

import lombok.Data;

@Data
public class CommentCountsWithBoardIdDto {
    private Long boardId;
    private long numberOfComments;

    public CommentCountsWithBoardIdDto(Long boardId, long numberOfComments) {
        this.boardId = boardId;
        this.numberOfComments = numberOfComments;
    }
}
