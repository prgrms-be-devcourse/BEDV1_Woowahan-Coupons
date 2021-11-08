package com.coumin.woowahancoupons.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.coumin.woowahancoupons.coupon.factory.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemptionRepository;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.customer.CustomerRepository;
import com.coumin.woowahancoupons.global.OptimisticLockTryer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class CouponLockTest {

    @Autowired
    private CouponRedemptionService couponRedemptionService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CouponRedemptionRepository couponRedemptionRepository;

    @Autowired
    private OptimisticLockTryer optimisticLockTryer;

    @AfterEach
    void afterEach() {
        couponRedemptionRepository.deleteAll();
        couponRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("고객에게 쿠폰 자동 할당 Lock 테스트 - 낙관적 락")
    void allocateCouponToCustomerWithIssuanceLockTest() throws InterruptedException {
        //Given
        Coupon savedCoupon = couponRepository.save(TestCouponFactory.builder().maxCount(10).build());
        Customer savedCustomer = customerRepository.save(new Customer("test@gmail.com"));

        //When
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                optimisticLockTryer.attempt(() ->
                        couponRedemptionService.allocateCouponToCustomerWithIssuance(
                            savedCoupon.getId(),
                            savedCustomer.getId())
                    , 10);

            });
        }
        Thread.sleep(1000);

        //Then
        Coupon findCoupon = couponRepository.findById(savedCoupon.getId()).get();
        assertThat(findCoupon.getAllocatedCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("쿠폰 코드 발행 Lock 테스트 - 비관적 락")
    void issueCouponCodesLockTest() throws InterruptedException {
        //Given
        Coupon savedCoupon = couponRepository.save(TestCouponFactory.builder().maxCount(30).build());

        //When
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 6; i++) {
            executorService.execute(() -> {
                couponRedemptionService.issueCouponCodes(
                    savedCoupon.getId(),
                    5);
            });
        }
        Thread.sleep(1000);

        //Then
        Coupon findCoupon = couponRepository.findById(savedCoupon.getId()).get();
        assertThat(findCoupon.getAllocatedCount()).isEqualTo(30);
    }
}
