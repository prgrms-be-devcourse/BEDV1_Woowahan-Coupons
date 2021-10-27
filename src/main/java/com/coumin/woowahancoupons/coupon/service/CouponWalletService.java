package com.coumin.woowahancoupons.coupon.service;

import java.util.UUID;

public interface CouponWalletService {
    void allocateCouponToCustomer(UUID couponId, Long customerId);
}
