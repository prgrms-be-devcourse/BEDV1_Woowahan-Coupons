package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class AdminNotFoundException extends EntityNotFoundException {

    public AdminNotFoundException(Long couponAdminId) {
        super(String.valueOf(couponAdminId), ErrorCode.ADMIN_NOT_FOUND);
    }
}
