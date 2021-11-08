package com.coumin.woowahancoupons.coupon.controller;

import com.coumin.woowahancoupons.coupon.dto.CouponCheckRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponIssuanceDto;
import com.coumin.woowahancoupons.coupon.service.CouponRedemptionService;
import com.coumin.woowahancoupons.global.ApiResponse;
import com.coumin.woowahancoupons.global.OptimisticLockTryer;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/coupons")
@RestController
public class CouponRedemptionRestController {

    private final CouponRedemptionService couponRedemptionService;

    private final OptimisticLockTryer optimisticLockTryer;

    public CouponRedemptionRestController(CouponRedemptionService couponRedemptionService,
        OptimisticLockTryer optimisticLockTryer) {
        this.couponRedemptionService = couponRedemptionService;
        this.optimisticLockTryer = optimisticLockTryer;
    }

    @PatchMapping("/{couponCode}/customers/{customerId}/register")
    public ApiResponse<Object> registerCouponCode(
        @PathVariable("couponCode") UUID couponCode,
        @PathVariable("customerId") Long customerId
    ) {
        couponRedemptionService.allocateExistingCouponToCustomer(couponCode, customerId);
        return ApiResponse.success();
    }

    @PostMapping("/{couponId}/customers/{customerId}/allocate")
    public ApiResponse<Object> allocateCoupon(
        @PathVariable("couponId") Long couponId,
        @PathVariable("customerId") Long customerId
    ) {
        optimisticLockTryer.attempt(
            () -> couponRedemptionService.allocateCouponToCustomerWithIssuance(couponId, customerId),
            10);
        return ApiResponse.success();
    }

    @GetMapping("/{customerId}")
    public ApiResponse<Object> getCustomerCouponRedemptions(@PathVariable("customerId") Long customerId) {
        return ApiResponse.success(couponRedemptionService.findCustomerCouponRedemptions(customerId));
    }

    @PostMapping("/{couponId}/issue")
    public ApiResponse<Object> issueCouponCodes(
        @PathVariable("couponId") Long couponId,
        @Valid @RequestBody CouponIssuanceDto couponIssuanceDto
    ) {
        couponRedemptionService.issueCouponCodes(couponId, couponIssuanceDto.getIssuanceCount());
        return ApiResponse.success();
    }

    @PostMapping("/{couponRedemptionId}/check")
    public ApiResponse<Object> checkCouponRedemptionForUse(
        @PathVariable("couponRedemptionId") Long couponRedemptionId,
        @Valid @RequestBody CouponCheckRequestDto couponCheckRequestDto
    ) {
        couponRedemptionService.checkCouponForUse(couponRedemptionId, couponCheckRequestDto);
        return ApiResponse.success();
    }

    @PostMapping("/{couponRedemptionId}/use")
    public ApiResponse<Object> userCouponRedemption(@PathVariable("couponRedemptionId") Long couponRedemptionId
    ) {
        couponRedemptionService.useCustomerCoupon(couponRedemptionId);
        return ApiResponse.success();
    }
}
