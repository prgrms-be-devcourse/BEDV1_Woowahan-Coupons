package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.dto.CouponCreateConverter;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequest;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponse;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponCreateService {

    private CouponRepository couponRepository;
    private CouponCreateConverter couponCreateConverter;

    public CouponCreateService(CouponRepository couponRepository,
                               CouponCreateConverter couponCreateConverter) {
        this.couponRepository = couponRepository;
        this.couponCreateConverter = couponCreateConverter;
    }

    public CouponCreateResponse generateCoupon(CouponCreateRequest couponCreateRequest) {
        Coupon coupon = couponCreateConverter.convertToCoupon(couponCreateRequest);
        Coupon saveToCoupon = couponRepository.save(coupon);
        return couponCreateConverter.convertToCouponCreateResponse(saveToCoupon);
    }
}
