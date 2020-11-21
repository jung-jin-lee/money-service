package com.example.money.common.code;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    TYPE_MISMATCH_ERROR("C001", "Type Mismatch Value"),
    MISSING_REQUEST_HEADER_ERROR("C002", "Missing Request Header"),
    INVALID_REQUEST_BODY_ERROR("C003", "Invalid Request Body"),
    METHOD_NOT_ALLOWED_ERROR("C004", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR("C005", "Oops! Something's wrong")
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