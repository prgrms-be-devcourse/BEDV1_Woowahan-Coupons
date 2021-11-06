package com.coumin.woowahancoupons.coupon.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCouponSaveDto {

    @Size(min = 2, max = 100)
    @NotEmpty
    private String name;

    @Max(10000)
    @Min(1000)
    private long amount;

    @Max(30)
    @Min(7)
    private int daysAfterIssuance;

    @Max(100000)
    @PositiveOrZero
    private Long minOrderPrice;

    @Builder
    public StoreCouponSaveDto(String name, long amount, int daysAfterIssuance,
        Long minOrderPrice) {
        this.name = name;
        this.amount = amount;
        this.daysAfterIssuance = daysAfterIssuance;
        this.minOrderPrice = minOrderPrice;
    }
}
