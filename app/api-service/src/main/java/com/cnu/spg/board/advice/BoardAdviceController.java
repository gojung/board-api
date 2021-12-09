package com.cnu.spg.board.advice;

import com.cnu.spg.board.exception.BoardNotFoundException;
import com.cnu.spg.board.exception.BoardTypeNotValidException;
import com.cnu.spg.board.exception.CategoryNotFoundException;
import com.cnu.spg.board.exception.CategoryOmittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BoardAdviceController {
    /**
     * 404 Not Found
     * board 정보를 받을 수 없음
     */
    @ExceptionHandler({BoardNotFoundException.class, CategoryNotFoundException.class})
    protected ResponseEntity<String> handleBoardNotFoundException(final RuntimeException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * 400 Bad Request
     * 사용자 요청 이슈
     */
    @ExceptionHandler({BoardTypeNotValidException.class, CategoryOmittedException.class})
    protected ResponseEntity<String> handleInvalidBoardTypeException(final RuntimeException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
