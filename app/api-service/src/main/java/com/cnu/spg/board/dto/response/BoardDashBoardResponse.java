package com.cnu.spg.board.dto.response;

import com.cnu.spg.board.dto.BoardDto;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class BoardDashBoardResponse {
    Page<BoardDto> boards;

    public BoardDashBoardResponse(Page<BoardDto> boards) {
        this.boards = boards;
    }
}
