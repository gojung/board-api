package com.cnu.spg.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailResponse {
    private Long id;

    private String title;

    private Long writerId;

    private String writerName;

    private String content;

    private List<CommentResponse> comments;
}
