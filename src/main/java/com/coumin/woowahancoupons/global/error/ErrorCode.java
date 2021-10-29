package com.coumin.woowahancoupons.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNEXPECTED(-1, "-1", "Unexpected exception occurred"),

    ENTITY_NOT_FOUND(400, "C001", " Entity Not Found"),
    COUPON_WALLET_NOT_FOUND(400, "C002", "Can not find a CouponWallet for id "),
    STORE_NOT_FOUND(400, "C003", "Can not find a Store for id "),

    INVALID_INPUT_VALUE(400, "C004", " Invalid Input Value");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
