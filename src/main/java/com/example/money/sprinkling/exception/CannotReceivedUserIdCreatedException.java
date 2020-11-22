package com.example.money.sprinkling.exception;

import lombok.Getter;

@Getter
public class CannotReceivedUserIdCreatedException extends RuntimeException {

    private String message;

    public CannotReceivedUserIdCreatedException(String message) {
        this.message = message;
    }

}
