package com.coumin.woowahancoupons.coupon.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCheckRequestDto {

    private Long storeId;

    private Long orderPrice;

    public CouponCheckRequestDto(Long storeId, Long orderPrice) {
        this.storeId = storeId;
        this.orderPrice = orderPrice;
    }
}
