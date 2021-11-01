package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CustomerNotFoundException extends EntityNotFoundException {

    public CustomerNotFoundException(Long targetId) {
        super(String.valueOf(targetId), ErrorCode.CUSTOMER_NOT_FOUND);
    }
}
