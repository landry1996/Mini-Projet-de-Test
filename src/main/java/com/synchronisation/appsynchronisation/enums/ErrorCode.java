package com.synchronisation.appsynchronisation.enums;


public enum ErrorCode {

    NOT_FOUND(404),
    INTERNAL_SERVE_ERROR(500),
    ALREADY_EXIST(208);
    public final int value;

    ErrorCode(int value) {
        this.value = value;
    }
}
