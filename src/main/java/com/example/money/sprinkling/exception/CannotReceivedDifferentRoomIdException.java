package com.example.money.sprinkling.exception;

import lombok.Getter;

@Getter
public class CannotReceivedDifferentRoomIdException extends RuntimeException {

    private String message;

    public CannotReceivedDifferentRoomIdException(String message) {
        this.message = message;
    }

}
