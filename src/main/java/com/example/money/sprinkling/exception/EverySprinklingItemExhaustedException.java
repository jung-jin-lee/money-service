package com.example.money.sprinkling.exception;

import lombok.Getter;

@Getter
public class EverySprinklingItemExhaustedException extends RuntimeException {

    private String message;

    public EverySprinklingItemExhaustedException(String message) {
        this.message = message;
    }

}
