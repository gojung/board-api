package com.cnu.spg.board.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BoardCommentCountProjection {
    private final Long id;
    private final long numberOfComments;

    @QueryProjection
    public BoardCommentCountProjection(Long id, long numberOfComments) {
        this.id = id;
        this.numberOfComments = numberOfComments;
    }
}
