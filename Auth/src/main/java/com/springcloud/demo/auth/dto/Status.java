package com.springcloud.demo.auth.dto;

public enum Status {
    SUCCESS(0),
    FAIL(1);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
