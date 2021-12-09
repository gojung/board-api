package com.cnu.spg.board.advice;

import com.cnu.spg.board.exception.CategoryNotFoundException;
import com.cnu.spg.comon.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CategoryAdviceController {

    /**
     * 404 Not Found
     * board 정보를 받을 수 없음
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    protected ResponseEntity<String> handleCategoryNotFoundExceptionHandler(final NotFoundException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}
