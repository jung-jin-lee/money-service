package com.example.money.sprinkling.exception;

import lombok.Getter;

@Getter
public class InvalidStatisticsViewableDateTimeException extends RuntimeException {

    private String message;

    public InvalidStatisticsViewableDateTimeException(String message) {
        this.message = message;
    }

}
