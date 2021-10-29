package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
