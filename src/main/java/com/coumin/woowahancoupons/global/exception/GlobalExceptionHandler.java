package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.ApiResponse;
import com.coumin.woowahancoupons.global.error.ErrorCode;
import com.coumin.woowahancoupons.global.exception.BusinessException;
import java.util.Arrays;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<?>> newResponseEntity(Throwable throwable, HttpStatus status) {
        Optional<String> message = Arrays.stream(throwable.getStackTrace())
            .map(StackTraceElement::toString).reduce((a, b) -> a + " " + b);
        return newResponseEntity(ErrorCode.UNEXPECTED.getCode(), message.orElse(throwable.getMessage()), status);
    }

    private ResponseEntity<ApiResponse<?>> newResponseEntity(String code, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(ApiResponse.error(code, message));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException e) {
        log.error("businessException exception occurred: {}", e.getMessage(), e);
        return newResponseEntity(e.getErrorCode().getCode(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return newResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
