package com.coumin.woowahancoupons.coupon.controller;

import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequest;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponse;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.coupon.service.CouponCreateService;
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
    private final CouponCreateService couponCreateService;

    public CouponRestController(CouponService couponService, CouponCreateService couponCreateService) {
        this.couponService = couponService;
        this.couponCreateService = couponCreateService;
    }

    @PostMapping("/{storeId}/issuance")
    public ApiResponse<Object> createStoreCoupons(
        @PathVariable long storeId,
        @RequestBody @Valid StoreCouponSaveRequestDto storeCouponSaveRequestDto) {

        couponService.saveAllStoreCoupons(storeId, storeCouponSaveRequestDto);
        return ApiResponse.success();
    }

    @PostMapping("/api/v1/coupons")
    public ApiResponse<CouponCreateResponse> save(
        @RequestBody CouponCreateRequest couponCreateDto
    ) {
        CouponCreateResponse couponCreateResponse = couponCreateService.generateCoupon(couponCreateDto);
        return ApiResponse.success(couponCreateResponse);
    }
}
