package com.cnu.spg.board.exception;

public class BoardTypeNotValidException extends RuntimeException {
    private static final String BOARD_TYPE_NOT_VALID_EXCEPTION_MSG = "지원하지 않는 board type 입니다.";

    public BoardTypeNotValidException() {
        super(BOARD_TYPE_NOT_VALID_EXCEPTION_MSG);
    }
}
