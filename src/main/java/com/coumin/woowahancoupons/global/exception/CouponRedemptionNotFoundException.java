package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;
import java.util.UUID;

public class CouponRedemptionNotFoundException extends EntityNotFoundException {

    public CouponRedemptionNotFoundException(Long targetId) {
        super(String.valueOf(targetId), ErrorCode.COUPON_REDEMPTION_NOT_FOUND);
    }

    public CouponRedemptionNotFoundException(UUID couponCode) {
        super(couponCode.toString(), ErrorCode.COUPON_REDEMPTION_NOT_FOUND);
    }
}
