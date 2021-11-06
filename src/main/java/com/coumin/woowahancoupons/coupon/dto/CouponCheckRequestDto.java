package com.coumin.woowahancoupons.coupon.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCheckRequestDto {

    @NotNull
    private Long storeId;

    @PositiveOrZero
    @NotNull
    private Long orderPrice;

    public CouponCheckRequestDto(Long storeId, Long orderPrice) {
        this.storeId = storeId;
        this.orderPrice = orderPrice;
    }
}
