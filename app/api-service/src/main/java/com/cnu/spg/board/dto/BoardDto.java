package com.cnu.spg.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BoardDto {
    private Long id;

    private String title;

    private Long writerId;

    private String writerName;

    private String content;

    private long commentCount;
}
