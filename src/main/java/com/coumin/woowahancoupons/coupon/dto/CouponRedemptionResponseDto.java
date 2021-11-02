package com.coumin.woowahancoupons.coupon.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponRedemptionResponseDto {

    private Long id;

    private UUID couponCode;

    private LocalDateTime startAt;

    private LocalDateTime expiredAt;

    private SimpleCouponDto coupon;

    public CouponRedemptionResponseDto(Long id, UUID couponCode, LocalDateTime startAt,
        LocalDateTime expiredAt, SimpleCouponDto coupon) {
        this.id = id;
        this.couponCode = couponCode;
        this.startAt = startAt;
        this.expiredAt = expiredAt;
        this.coupon = coupon;
    }
}
