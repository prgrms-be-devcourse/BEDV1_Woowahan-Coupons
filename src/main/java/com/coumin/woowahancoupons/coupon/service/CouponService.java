package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;

public interface CouponService {

    void saveAllStoreCoupons(Long storeId, StoreCouponSaveRequestDto requestDto);
}
