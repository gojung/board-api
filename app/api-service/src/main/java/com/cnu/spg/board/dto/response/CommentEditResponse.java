package com.cnu.spg.board.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentEditResponse {
    private final Long id;
    private final String content;

    public static CommentEditResponse of(Long commentId, String content) {
        return new CommentEditResponse(commentId, content);
    }
}
