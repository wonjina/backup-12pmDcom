package com.gabia.project.internproject.common.exception;

import com.gabia.project.internproject.controller.dto.ErrorResponseDto;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getBindingResult());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnknownUserException.class)
    public ResponseEntity<?> UnknownUserExceptionException(Exception e) {
        log.error("UnknownUserExceptionException", e);
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ DataAccessException.class, RuntimeException.class })
    public ResponseEntity<?> handleSQLException(Exception e) {
        log.error("handleSQLException", e);
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponseDto> handleBindException(Exception e) {
        log.error("handleBindException", e);
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorResponseDto> handleJwtException(Exception e) {
        log.error("JwtException", e);
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(Exception e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException e) {
        log.error("ValidationException", e);
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            message.append(violation.getMessage());
        }
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.PRECONDITION_FAILED, message.toString());
        return new ResponseEntity(response, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponseDto> handleBusinessException(final BusinessException e) {
        log.error("handleEntityNotFoundException", e);
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler()
    protected ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error("handleException", e);
        final ErrorResponseDto response = ErrorResponseDto.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
