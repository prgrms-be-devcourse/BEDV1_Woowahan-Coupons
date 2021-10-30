package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponseDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import org.springframework.transaction.annotation.Transactional;

public interface CouponService {

    void saveAllStoreCoupons(Long storeId, StoreCouponSaveRequestDto requestDto);

    CouponCreateResponseDto generateCoupon(CouponCreateRequestDto couponCreateRequest);
}
