package com.cnu.spg.comment.exception;

import com.cnu.spg.comon.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    private static final String COMMENT_NOT_FOUND_EXCEPTION_MSG = "commnet 정보를 찾지 못 하였습니다.";

    public CommentNotFoundException() {
        super(COMMENT_NOT_FOUND_EXCEPTION_MSG);
    }
}
