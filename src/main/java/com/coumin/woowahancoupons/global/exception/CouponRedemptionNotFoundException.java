package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;
import java.util.UUID;

public class CouponRedemptionNotFoundException extends EntityNotFoundException{

    public CouponRedemptionNotFoundException(UUID targetId) {
        super(String.valueOf(targetId), ErrorCode.COUPON_REDEMPTION_NOT_FOUND);
    }
}
