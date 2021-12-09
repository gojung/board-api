package com.cnu.spg.board.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentRequest {
    @NotBlank
    @NotNull
    private String content;

    @NotNull
    private Long boardId;
}
