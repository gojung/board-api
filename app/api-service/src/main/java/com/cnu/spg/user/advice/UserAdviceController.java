package com.cnu.spg.user.advice;

import com.cnu.spg.user.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UserAdviceController {
    /**
     * 404 Not Found
     * board 정보를 받을 수 없음
     */
    @ExceptionHandler({RoleNotFoundException.class, UserNotFoundException.class})
    protected ResponseEntity<String> handleNotFoundException(final RuntimeException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * 400 Bad Request
     * password 정보 에러
     */
    @ExceptionHandler({
            PasswordNotMatchException.class,
            UserParamterOmittedException.class,
            UserTypeIsNotValid.class,
            UsernameAlreadyExistException.class,
            PasswordNotConfirmException.class
    })
    protected ResponseEntity<String> handleBadRequestException(final RuntimeException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    /**
     * 401 Unauthorized
     * token 값이 적절하지 않음
     */
    @ExceptionHandler(TokenIsNotValidException.class)
    protected ResponseEntity<String> handleTokenValidationException(final RuntimeException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}