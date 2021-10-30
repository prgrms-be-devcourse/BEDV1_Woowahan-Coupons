package com.coumin.woowahancoupons.coupon.controller;

import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponseDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.coupon.service.CouponService;
import com.coumin.woowahancoupons.global.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
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
        @RequestBody @Size(min = 1, max = 3) List<@Valid StoreCouponSaveRequestDto> storeCouponSaveRequestDto ) {

        couponService.saveAllStoreCoupons(storeId, storeCouponSaveRequestDto);
        return ApiResponse.success();
    }

    @PostMapping
    public ApiResponse<CouponCreateResponseDto> save(
        @RequestBody CouponCreateRequestDto couponCreateDto
    ) {
        CouponCreateResponseDto couponCreateResponse = couponService.generateCoupon(couponCreateDto);
        return ApiResponse.success(couponCreateResponse);
    }
}
