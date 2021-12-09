package com.cnu.spg.board.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentEditRequest {
    @NotNull(message = "공백이 아닌 정보를 넘겨주세요")
    @NotBlank(message = "공백이 아닌 정보를 넘겨주세요")
    private String content;
}
