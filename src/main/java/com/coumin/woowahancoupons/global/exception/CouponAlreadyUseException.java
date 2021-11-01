package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CouponAlreadyUseException extends BusinessException {

    public CouponAlreadyUseException() {
        super(ErrorCode.COUPON_REDEMPTION_ALREADY_USE);
    }
}
