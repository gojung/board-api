package com.cnu.spg.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDto {
    private Long id;
    private String name;
    private String comment;
}
