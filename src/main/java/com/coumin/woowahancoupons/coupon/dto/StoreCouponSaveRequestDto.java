package com.coumin.woowahancoupons.coupon.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCouponSaveRequestDto {

    @Valid
    @Size(min = 1, max = 3)
    private List<StoreCouponSaveDto> storeCouponSaves;

    public StoreCouponSaveRequestDto(List<StoreCouponSaveDto> storeCouponSaves) {
        this.storeCouponSaves = storeCouponSaves;
    }
}
