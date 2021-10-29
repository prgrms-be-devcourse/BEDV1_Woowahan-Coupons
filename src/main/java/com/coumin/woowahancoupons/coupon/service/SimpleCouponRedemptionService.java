package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemptionRepository;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.customer.CustomerRepository;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionNotFoundException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SimpleCouponRedemptionService implements CouponRedemptionService {

    private final CouponRedemptionRepository couponRedemptionRepository;
    private final CustomerRepository customerRepository;

    public SimpleCouponRedemptionService(
        CouponRedemptionRepository couponRedemptionRepository,
        CustomerRepository customerRepository) {
        this.couponRedemptionRepository = couponRedemptionRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Override
    public void allocateCouponToCustomer(UUID couponId, Long customerId) {
        CouponRedemption couponRedemption = couponRedemptionRepository.findById(couponId)
            .orElseThrow(() -> new CouponRedemptionNotFoundException(couponId));
        Customer customer = customerRepository.getById(customerId);
        couponRedemption.changeCustomer(customer);
    }
}
