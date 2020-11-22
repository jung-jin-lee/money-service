package com.example.money.sprinkling.exception;

import lombok.Getter;

@Getter
public class AlreadyReceivedException extends RuntimeException {

    private String message;

    public AlreadyReceivedException(String message) {
        this.message = message;
    }

}
