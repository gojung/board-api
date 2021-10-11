package com.cnu.spg.board.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BoardTypeRequset {
    private String partTitle;
    private String writerName;
    private String partOfContent;
    @NotNull(message = "category 정보를 넣어주세요")
    private Long categoryId;
}
