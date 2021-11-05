package com.coumin.woowahancoupons.coupon.dto;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCheckRequestDto {

    @NotNull
    private Long storeId;

    @NotNull
    private Long orderPrice;

    public CouponCheckRequestDto(Long storeId, Long orderPrice) {
        this.storeId = storeId;
        this.orderPrice = orderPrice;
    }
}
