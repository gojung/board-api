package com.cnu.spg.board.exception;

public class BoardTypeNotMatchException extends RuntimeException {
    private static final String BOARD_TYPE_ERROR_EXCEPTION_MSG = "board type 이 적절하지 않습니다";

    public BoardTypeNotMatchException() {
        super(BOARD_TYPE_ERROR_EXCEPTION_MSG);
    }
}
