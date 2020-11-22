package com.example.money.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private String resource;
    private String message;

    public NotFoundException(String resource, String message) {
        this.resource = resource;
        this.message = message;
    }
}
