package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CouponNotFoundException extends EntityNotFoundException {

    public CouponNotFoundException(Long targetId) {
        super(String.valueOf(targetId), ErrorCode.COUPON_NOT_FOUND);
    }
}
