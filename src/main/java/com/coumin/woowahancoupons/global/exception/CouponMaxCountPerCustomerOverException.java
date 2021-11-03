package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CouponMaxCountPerCustomerOverException extends BusinessException {

    public CouponMaxCountPerCustomerOverException(int maxCountPerCustomer) {
        super(ErrorCode.COUPON_MAX_COUNT_OVER, maxCountPerCustomer);
    }
}
