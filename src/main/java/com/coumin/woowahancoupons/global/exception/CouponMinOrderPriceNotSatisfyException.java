package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CouponMinOrderPriceNotSatisfyException extends BusinessException {

    public CouponMinOrderPriceNotSatisfyException(long minOrderPrice) {
        super(ErrorCode.COUPON_MIN_ORDER_PRICE_NOT_SATISFY, minOrderPrice);
    }
}
