package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.domain.coupon.CouponWallet;
import com.coumin.woowahancoupons.domain.coupon.CouponWalletRepository;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.customer.CustomerRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SimpleCouponWalletService implements CouponWalletService {

    private final CouponWalletRepository couponWalletRepository;
    private final CustomerRepository customerRepository;

    public SimpleCouponWalletService(
        CouponWalletRepository couponWalletRepository,
        CustomerRepository customerRepository) {
        this.couponWalletRepository = couponWalletRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Override
    public void allocateCouponToCustomer(UUID couponId, Long customerId) {
        CouponWallet coupon = couponWalletRepository.findById(couponId).orElseThrow();
        Customer customer = customerRepository.getById(customerId);
        coupon.changeCustomer(customer);
    }
}
