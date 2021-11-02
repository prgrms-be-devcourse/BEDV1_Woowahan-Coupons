package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CouponMaxCountOverException extends BusinessException {

    public CouponMaxCountOverException(int maxCount, int allocatedCount, int issuanceCount) {
        super(ErrorCode.COUPON_MAX_COUNT_OVER, maxCount, allocatedCount, issuanceCount);
    }
}
