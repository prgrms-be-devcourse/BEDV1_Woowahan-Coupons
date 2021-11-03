package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.dto.CouponRedemptionResponseDto;
import java.util.List;
import java.util.UUID;

public interface CouponRedemptionService {

    void allocateExistingCouponToCustomer(UUID couponCode, Long customerId);

    void allocateCouponToCustomerWithIssuance(Long couponId, Long customerId);

    List<CouponRedemptionResponseDto> findCustomerCouponRedemptions(Long customerId);

    void issueCouponCodes(Long couponId, int issuanceCount);

    void useCustomerCoupon(Long couponRedemptionId);
}
