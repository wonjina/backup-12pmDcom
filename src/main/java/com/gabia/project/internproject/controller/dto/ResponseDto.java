package com.gabia.project.internproject.controller.dto;

import lombok.Getter;

@Getter
public class ResponseDto<T> {

    private T response;

    public ResponseDto(T t){
        this.response = t;
    }

}