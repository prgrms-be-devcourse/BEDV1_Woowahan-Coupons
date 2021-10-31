package com.coumin.woowahancoupons.coupon.controller;

import com.coumin.woowahancoupons.coupon.service.CouponRedemptionService;
import com.coumin.woowahancoupons.global.ApiResponse;
import java.util.UUID;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/coupons")
@RestController
public class CouponRedemptionRestController {

    private final CouponRedemptionService couponRedemptionService;

    public CouponRedemptionRestController(
        CouponRedemptionService couponRedemptionService) {
        this.couponRedemptionService = couponRedemptionService;
    }

    @PatchMapping("/{couponCode}/customers/{customerId}/register")
    public ApiResponse<Object> registerCouponCode(
        @PathVariable("couponCode") UUID couponCode,
        @PathVariable("customerId") Long customerId) {
        couponRedemptionService.allocateExistingCouponToCustomer(couponCode, customerId);
        return ApiResponse.success();
    }
}
