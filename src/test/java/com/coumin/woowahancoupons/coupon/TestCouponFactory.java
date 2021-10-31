package com.coumin.woowahancoupons.coupon;

import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import java.time.LocalDateTime;

public class TestCouponFactory {

    public static Coupon.CouponBuilder builder() {
        return Coupon.builder("test coupon",
                1000L,
                ExpirationPolicy.newByPeriod(LocalDateTime.now(), LocalDateTime.now().plusDays(30)),
                DiscountType.FIXED_AMOUNT,
                IssuerType.ADMIN,
                1L);
    }
}
