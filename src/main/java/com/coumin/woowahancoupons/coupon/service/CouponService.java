package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponseDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import java.util.List;

public interface CouponService {

    void saveAllStoreCoupons(Long storeId, List<StoreCouponSaveRequestDto> requestDtoList);

    CouponCreateResponseDto generateCoupon(CouponCreateRequestDto couponCreateRequest);
}
