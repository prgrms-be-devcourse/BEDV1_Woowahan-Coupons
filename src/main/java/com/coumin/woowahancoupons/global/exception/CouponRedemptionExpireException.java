package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CouponRedemptionExpireException extends BusinessException {

    public CouponRedemptionExpireException() {
        super(ErrorCode.COUPON_REDEMPTION_EXPIRE);
    }
}
