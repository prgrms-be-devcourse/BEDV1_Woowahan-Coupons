package com.coumin.woowahancoupons.coupon.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleCouponDto {

    private String name;

    private long amount;

    private Long minOrderPrice;

    private String discountType;

    private String issuerType;

    public SimpleCouponDto(String name, Long amount, Long minOrderPrice, String discountType,
        String issuerType) {
        this.name = name;
        this.amount = amount;
        this.minOrderPrice = minOrderPrice;
        this.discountType = discountType;
        this.issuerType = issuerType;
    }
}
