package com.coumin.woowahancoupons.coupon.dto;

import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssuanceDto {

    @Positive
    private int issuanceCount;

    public CouponIssuanceDto(int issuanceCount) {
        this.issuanceCount = issuanceCount;
    }
}
