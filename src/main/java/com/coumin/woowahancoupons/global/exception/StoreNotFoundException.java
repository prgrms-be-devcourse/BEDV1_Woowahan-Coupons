package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class StoreNotFoundException extends EntityNotFoundException {

    public StoreNotFoundException(Long targetId) {
        super(String.valueOf(targetId), ErrorCode.STORE_NOT_FOUND);
    }
}
