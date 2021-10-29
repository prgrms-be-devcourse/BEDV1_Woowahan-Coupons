package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;
import java.util.UUID;

public class CouponWalletNotFoundException extends EntityNotFoundException{

    public CouponWalletNotFoundException(UUID targetId) {
        super(String.valueOf(targetId), ErrorCode.COUPON_WALLET_NOT_FOUND);
    }
}
