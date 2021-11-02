package com.coumin.woowahancoupons.coupon.converter;

import com.coumin.woowahancoupons.coupon.dto.CouponRedemptionResponseDto;
import com.coumin.woowahancoupons.coupon.dto.SimpleCouponDto;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import org.springframework.stereotype.Component;

@Component
public class CouponRedemptionConverter {
    public CouponRedemptionResponseDto convertToCouponRedemptionResponseDto(
        CouponRedemption couponRedemption) {
        return new CouponRedemptionResponseDto(
            couponRedemption.getId(),
            couponRedemption.getCouponCode(),
            couponRedemption.getExpirationPeriod().getStartAt(),
            couponRedemption.getExpirationPeriod().getExpiredAt(),
            convertToSimpleCouponDto(couponRedemption.getCoupon())
        );
    }

    private SimpleCouponDto convertToSimpleCouponDto(Coupon coupon) {
        return new SimpleCouponDto(
            coupon.getName(),
            coupon.getAmount(),
            coupon.getMinOrderPrice(),
            coupon.getDiscountType().name(),
            coupon.getIssuerType().name()
        );
    }
}
