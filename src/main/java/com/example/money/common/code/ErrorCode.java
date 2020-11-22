package com.example.money.common.code;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // common
    TYPE_MISMATCH_ERROR("C001", "Type Mismatch Value"),
    MISSING_REQUEST_HEADER_ERROR("C002", "Missing Request Header"),
    INVALID_REQUEST_BODY_ERROR("C003", "Invalid Request Body"),
    METHOD_NOT_ALLOWED_ERROR("C004", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR("C005", "Oops! Something's wrong"),
    NOT_FOUND_ERROR("C006", "Not Found"),

    // sprinkling
    SPRINKLING_ALREADY_RECEIVED_ERROR("S001", "Already Sprinkling Received"),
    SPRINKLING_CANNOT_RECEIVED_USER_ID_CREATED_ERROR("S002", "Whoever created the sprinkling cannot receive it"),
    SPRINKLING_INVALID_RECEIVE_DATE_TIME_ERROR("S003", "Receiving is only valid for 10 minutes after sprinkling"),
    SPRINKLING_CANNOT_RECEIVED_DIFFERENT_ROOM_ID_ERROR("S004", "If room ID of sprinkling is different, you cannot receive it"),
    SPRINKLING_EXHAUSTED_ERROR("S005", "All the sprinkles that can already be received are exhausted"),
    SPRINKLING_INVALID_STATISTICS_VIEWABLE_DATE_TIME_ERROR("S006", "Statistics viewing is possible only for 7 days after the sprinkling is created.")
    ;



    private final String code;
    private final String message;

    ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}