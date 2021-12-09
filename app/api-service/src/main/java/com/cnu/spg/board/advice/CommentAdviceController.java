package com.cnu.spg.board.advice;

import com.cnu.spg.comment.exception.CommentAuthException;
import com.cnu.spg.comment.exception.CommentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommentAdviceController {

    /**
     * 403 Forbidden
     * 내가 작성하지 않은 댓글 변경
     */
    @ExceptionHandler(CommentAuthException.class)
    protected ResponseEntity<String> commentAuthExceptionHandler(RuntimeException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    /**
     * 404 Not Found
     * comment 정보가 없을 경우
     */
    @ExceptionHandler(CommentNotFoundException.class)
    protected ResponseEntity<String> commentNotFoundExceptionHandler(RuntimeException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
