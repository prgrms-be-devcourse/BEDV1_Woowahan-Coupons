package com.coumin.woowahancoupons.coupon.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCheckRequestDto {

    private Long storeId;

    private Long orderPrice;

}
