package com.coumin.woowahancoupons.coupon.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.coumin.woowahancoupons.domain.coupon.Coupon;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCouponResponseDto {

    private long id;

    private String name;

    private long amount;

    private int daysAfterIssuance;

    private Long minOrderPrice;

    public StoreCouponResponseDto(Coupon coupon) {
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.amount = coupon.getAmount();
        this.daysAfterIssuance = coupon.getExpirationPolicy().getDaysFromIssuance();
        this.minOrderPrice = coupon.getMinOrderPrice();
    }
}
