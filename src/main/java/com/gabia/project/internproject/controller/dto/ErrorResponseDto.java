package com.gabia.project.internproject.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  exception/GlobalExceptionHandler.class 에서 예외에 대한 응답 DTO
 *
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ErrorResponseDto {

    private String message;
    private int code;
    private List<FieldError> errors;

    private ErrorResponseDto(final HttpStatus code, final List<FieldError> errors) {
        this.message = code.getReasonPhrase();
        this.errors = errors;
        this.code = code.value();
    }

    public ErrorResponseDto(final HttpStatus code) {
        this.message = code.getReasonPhrase();
        this.code = code.value();
        this.errors = new ArrayList<>();
    }

    public ErrorResponseDto(final HttpStatus code, String msg) {
        this.message = msg;
        this.code = code.value();
        this.errors = new ArrayList<>();
    }

    public static ErrorResponseDto of(final HttpStatus code, String msg) {
        return new ErrorResponseDto(code, msg);
    }

    public static ErrorResponseDto of(final HttpStatus code, final BindingResult bindingResult) {
        return new ErrorResponseDto(code, FieldError.of(bindingResult));
    }

    public static ErrorResponseDto of(final HttpStatus code) {
        return new ErrorResponseDto(code);
    }

    public static ErrorResponseDto of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<ErrorResponseDto.FieldError> errors = ErrorResponseDto.FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST, errors);
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
