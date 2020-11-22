package com.example.money.sprinkling.exception;

import lombok.Getter;

@Getter
public class InvalidReceiveDateTimeException extends RuntimeException {

    private String message;

    public InvalidReceiveDateTimeException(String message) {
        this.message = message;
    }

}
