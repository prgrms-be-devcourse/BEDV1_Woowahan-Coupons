package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CouponExpireException extends BusinessException {

    public CouponExpireException() {
        super(ErrorCode.COUPON_REDEMPTION_EXPIRE);
    }
}
