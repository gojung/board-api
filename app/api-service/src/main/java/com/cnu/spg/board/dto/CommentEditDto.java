package com.cnu.spg.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentEditDto {
    private final Long userId;
    private final Long boardId;
    private final Long commentId;
    private final String newContent;

    public static CommentEditDto of(Long userId, Long boardId, Long commentId, String userContent) {
        return new CommentEditDto(userId, boardId, commentId, userContent);
    }
}
