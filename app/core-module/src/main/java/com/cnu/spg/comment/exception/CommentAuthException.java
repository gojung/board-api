package com.cnu.spg.comment.exception;

public class CommentAuthException extends RuntimeException {
    private static final String COMMENT_AUTH_EXCEPTION_MSG = "댓글 수정 권한이 없습니다";

    public CommentAuthException() {
        super(COMMENT_AUTH_EXCEPTION_MSG);
    }
}
