package com.coumin.woowahancoupons.coupon.dto;

import java.util.List;

public class StoreCouponSaveRequestDto {

    private final List<StoreCouponSaveDto> storeCouponSaveDtos;

    public StoreCouponSaveRequestDto(
        List<StoreCouponSaveDto> storeCouponSaveDtos) {
        this.storeCouponSaveDtos = storeCouponSaveDtos;
    }

    public List<StoreCouponSaveDto> getStoreCouponSaveDtos() {
        return storeCouponSaveDtos;
    }
}
