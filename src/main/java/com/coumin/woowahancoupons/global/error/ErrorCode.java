package com.coumin.woowahancoupons.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNEXPECTED(-1, "-1", "Unexpected exception occurred"),

    ENTITY_NOT_FOUND(400, "C001", "Can not find a Entity"),
    COUPON_REDEMPTION_NOT_FOUND(400, "C002", "Can not find a CouponRedemption for id "),
    STORE_NOT_FOUND(400, "C003", "Can not find a Store for id "),
    COUPON_NOT_FOUND(400, "CP01", "Can not find a Coupon for id "),
    CUSTOMER_NOT_FOUND(400, "CT01", "Can not find a Customer for id "),
    ADMIN_NOT_FOUND(400, "AI01", "Can not find a Admin for id "),

    INVALID_INPUT_VALUE(400, "C004", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C005", "Method not allowed"),

    COUPON_REDEMPTION_ALREADY_ALLOCATE(400, "CO06", "CouponRedemption was already allocated"),
    COUPON_REDEMPTION_ALREADY_USE(400, "CO07", "CouponRedemption was already used"),
    COUPON_REDEMPTION_EXPIRE(400, "CO08", "CouponRedemption was already expired"),
    COUPON_MAX_COUNT_OVER(400, "CP02", "Can not issue coupon codes. coupon's maxCount : %d, allocatedCount : %d, but your request count : %d"),

    COUPON_ISSUER_ID_NOT_MATCH(400, "C009", "Can only be used by %s(%d)"),
    COUPON_MIN_ORDER_PRICE_NOT_SATISFY(400, "C010", "Can be used when order price is more than %d원"),
    COUPON_MAX_COUNT_PER_CUSTOMER_OVER(400, "CP03", "Can not issue a coupon code to customer. the customer's coupon count has already reached the coupon's maxCountPerCustomer %d");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
