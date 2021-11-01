package com.coumin.woowahancoupons.coupon.controller;

import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponseDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.coupon.service.CouponService;
import com.coumin.woowahancoupons.global.ApiResponse;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/coupons")
public class CouponRestController {

    private final CouponService couponService;

    public CouponRestController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/{storeId}/issuance")
    public ApiResponse<Object> createStoreCoupons(
        @PathVariable long storeId,
        @RequestBody @Valid StoreCouponSaveRequestDto storeCouponSaveRequestDto
    ) {
        couponService.saveAllStoreCoupons(storeId, storeCouponSaveRequestDto);
        return ApiResponse.success();
    }

    @PostMapping("/{couponAdminId}")
    public ApiResponse<CouponCreateResponseDto> createAdminCoupon(
        @PathVariable Long couponAdminId,
        @RequestBody CouponCreateRequestDto couponCreateRequestDto
    ) {
        CouponCreateResponseDto couponCreateResponse = couponService.generateCoupon(couponCreateRequestDto, couponAdminId);
        return ApiResponse.success(couponCreateResponse);
    }
}
