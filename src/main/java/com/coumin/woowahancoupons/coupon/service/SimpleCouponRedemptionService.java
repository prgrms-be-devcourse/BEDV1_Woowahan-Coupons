package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemptionRepository;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.customer.CustomerRepository;
import com.coumin.woowahancoupons.global.exception.CouponMaxCountOverException;
import com.coumin.woowahancoupons.global.exception.CouponNotFoundException;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionNotFoundException;
import com.coumin.woowahancoupons.global.exception.CustomerNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SimpleCouponRedemptionService implements CouponRedemptionService {

    private final CouponRedemptionRepository couponRedemptionRepository;

    private final CustomerRepository customerRepository;

    private final CouponRepository couponRepository;

    public SimpleCouponRedemptionService(
        CouponRedemptionRepository couponRedemptionRepository,
        CustomerRepository customerRepository,
        CouponRepository couponRepository
    ) {
        this.couponRedemptionRepository = couponRedemptionRepository;
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
    }

    @Transactional
    @Override
    public void allocateExistingCouponToCustomer(UUID couponCode, Long customerId) {
        CouponRedemption couponRedemption = couponRedemptionRepository.findByCouponCode(couponCode)
            .orElseThrow(() -> new CouponRedemptionNotFoundException(couponCode));
        Customer customer = customerRepository.getById(customerId);
        couponRedemption.allocateCustomer(customer);
    }

    @Transactional
    @Override
    public void allocateCouponToCustomerWithIssuance(Long couponId, Long customerId) {
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new CouponNotFoundException(couponId));
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
        couponRedemptionRepository.save(CouponRedemption.of(coupon, customer));
    }

    @Transactional
    @Override
    public void issueCouponCodes(Long couponId, int issueCount) {
        Coupon coupon = couponRepository.findByIdForUpdate(couponId)
            .orElseThrow(() -> new CouponNotFoundException(couponId));
        if (!coupon.canIssueCouponCodes(issueCount)) {
            throw new CouponMaxCountOverException(coupon.getMaxCount(), coupon.getAllocatedCount(), issueCount);
        }
        List<CouponRedemption> couponRedemptions = IntStream.rangeClosed(1, issueCount)
            .mapToObj(operand -> CouponRedemption.of(coupon))
            .collect(Collectors.toList());
        int insertSize = couponRedemptionRepository.saveAll(couponRedemptions).size();
        coupon.increaseAllocatedCount(insertSize);
    }
}
