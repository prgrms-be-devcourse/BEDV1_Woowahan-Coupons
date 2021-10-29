package com.coumin.woowahancoupons.global.error;

import com.coumin.woowahancoupons.global.ApiResponse;
import com.coumin.woowahancoupons.global.exception.BusinessException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<ApiResponse<?>> newResponseEntity(Throwable throwable, HttpStatus status) {
		Optional<String> message = Arrays.stream(throwable.getStackTrace())
			.map(StackTraceElement::toString)
			.reduce((a, b) -> a + " " + b);

		return newResponseEntity(ErrorCode.UNEXPECTED.getCode(),
			message.orElse(throwable.getMessage()), status);
	}

	private ResponseEntity<ApiResponse<?>> newResponseEntity(String code, String message, HttpStatus status) {
		return ResponseEntity.status(status).body(ApiResponse.error(code, message));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

		String errorDetail = e.getBindingResult().getFieldErrors()
			.stream()
			.map(error -> String.format("%s %s", error.getField(), error.getDefaultMessage()))
			.collect(Collectors.joining(","));
		String errorMessage = String.format("%s: %s", errorCode.getMessage(), errorDetail);
		log.error("handleMethodArgumentNotValid exception occurred: {}", errorMessage, e);

		return newResponseEntity(errorCode.getCode(), errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("handleHttpRequestMethodNotSupportedException", e);
		ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;

		return newResponseEntity(errorCode.getCode(), errorCode.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
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
