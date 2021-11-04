package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class CouponIssuerIdNotMatchException extends BusinessException {

    public CouponIssuerIdNotMatchException(String issuerType, long issuerId) {
        super(ErrorCode.COUPON_ISSUER_ID_NOT_MATCH, issuerType, issuerId);
    }
}
