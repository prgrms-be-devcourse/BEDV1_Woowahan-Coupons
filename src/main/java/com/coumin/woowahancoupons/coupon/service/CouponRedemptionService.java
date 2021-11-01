package com.coumin.woowahancoupons.coupon.service;

import java.util.UUID;

public interface CouponRedemptionService {

    void allocateExistingCouponToCustomer(UUID couponCode, Long customerId);

    void allocateCouponToCustomerWithIssuance(Long couponId, Long customerId);

    void issueCouponCode(Long couponId);
}
