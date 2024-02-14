package com.springcloud.demo.auth.dto;

public enum ErrorCode {
    SUCCESS(0),
    USER_PASS_INCORRECT(100),
    USER_NOT_FOUND(101),
    TOKEN_INVALID(102);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}