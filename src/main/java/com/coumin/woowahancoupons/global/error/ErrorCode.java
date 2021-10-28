package com.coumin.woowahancoupons.global.error;

public enum ErrorCode {

    UNEXPECTED(-1, "-1", "Unexpected exception occurred"),
    ENTITY_NOT_FOUND(400, "C001", " Entity Not Found");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
