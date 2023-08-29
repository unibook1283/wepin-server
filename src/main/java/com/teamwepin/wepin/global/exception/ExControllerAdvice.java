package com.teamwepin.wepin.global.exception;

import com.teamwepin.wepin.domain.jwt.exception.CustomJwtException;
import com.teamwepin.wepin.global.response.dto.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    /**
     * filter에서 발생하는 exception들은 ExceptionHandlingFilter에서 처리되도록 했음.
     * controller에서 발생할 auth 관련 exception들을 위해 남겨놨으나 나중에 안 쓰면 지울 것.
     * CustomJwtException, AuthenticationException 둘 다.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResult unauthorizedExHandler(CustomJwtException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResult unauthorizedExHandler(AuthenticationException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("AUTH", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult badRequestExceptionHandler(BadRequestException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("ILL-ARG", e.getMessage());
    }

    /**
     * Spring validation 유효성 검증 실패 시 발생하는 exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult methodArgumentNotValidExHandler(MethodArgumentNotValidException e) {
        log.error("[exceptionHandler] ex", e);

        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        String fieldName = ((FieldError) objectError).getField();
        String message = objectError.getDefaultMessage();

        return new ErrorResult("ARG-NOT-VALID", fieldName + ": " + message);
    }

    /**
     * 위에서 걸러지지 못한 예외는 여기서 처리
     * 발생 시 어떤 이유인지 확인해야함.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

}
