package com.gabia.project.internproject.common.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String errMsg) {
        super(errMsg);
    }
}
