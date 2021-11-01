package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponseDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;

public interface CouponService {

    void saveAllStoreCoupons(Long storeId, StoreCouponSaveRequestDto requestDto);

    CouponCreateResponseDto generateCoupon(CouponCreateRequestDto couponCreateRequest, Long couponAdminId);
}
