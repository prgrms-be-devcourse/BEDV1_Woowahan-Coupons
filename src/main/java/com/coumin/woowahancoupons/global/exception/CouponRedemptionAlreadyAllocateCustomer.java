package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CouponRedemptionAlreadyAllocateCustomer extends BusinessException {

    public CouponRedemptionAlreadyAllocateCustomer() {
        super(ErrorCode.COUPON_REDEMPTION_ALREADY_ALLOCATE);
    }
}
