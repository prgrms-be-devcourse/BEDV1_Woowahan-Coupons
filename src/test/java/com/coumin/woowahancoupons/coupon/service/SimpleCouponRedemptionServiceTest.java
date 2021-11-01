package com.coumin.woowahancoupons.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.coumin.woowahancoupons.coupon.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemptionRepository;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.customer.CustomerRepository;
import com.coumin.woowahancoupons.global.error.ErrorCode;
import com.coumin.woowahancoupons.global.exception.CouponNotFoundException;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionAlreadyAllocateCustomer;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionNotFoundException;
import com.coumin.woowahancoupons.global.exception.CustomerNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleCouponRedemptionServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponRedemptionRepository couponRedemptionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private SimpleCouponRedemptionService couponRedemptionService;

    @Test
    @DisplayName("고객이 쿠폰 코드를 입력해 쿠폰 등록 - 성공 테스트")
    void allocateExistingCouponToCustomerSuccessTest() {
        //Given
        Long customerId = 1L;
        Customer mockCustomer = mock(Customer.class);
        Coupon coupon = TestCouponFactory.builder().build();
        CouponRedemption couponRedemption = CouponRedemption.of(coupon);

        given(couponRedemptionRepository.findByCouponCode(couponRedemption.getCouponCode()))
            .willReturn(Optional.of(couponRedemption));
        given(customerRepository.getById(customerId)).willReturn(mockCustomer);
        given(mockCustomer.getId()).willReturn(customerId);

        //When
        assertThat(couponRedemption.getCustomer()).isNull();
        couponRedemptionService.allocateExistingCouponToCustomer(
            couponRedemption.getCouponCode(),
            customerId
        );

        //Then
        assertThat(couponRedemption.getCustomer()).isNotNull();
        assertThat(couponRedemption.getCustomer().getId()).isEqualTo(customerId);
    }

    @Test
    @DisplayName("고객이 쿠폰 코드를 입력해 쿠폰 등록 - 실패 테스트 (잘못된 쿠폰 코드)")
    void allocateExistingCouponToCustomerFailureTest() {
        //Given
        UUID invalidCouponCode = UUID.randomUUID();
        given(couponRedemptionRepository.findByCouponCode(invalidCouponCode)).willReturn(Optional.empty());

        //When Then
        assertThatThrownBy(
            () -> couponRedemptionService.allocateExistingCouponToCustomer(invalidCouponCode, 1L))
            .isInstanceOf(CouponRedemptionNotFoundException.class)
            .hasMessageContaining(ErrorCode.COUPON_REDEMPTION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("고객이 쿠폰 코드를 입력해 쿠폰 등록 - 실패 테스트 (이미 등록된 쿠폰)")
    void allocateExistingCouponToCustomerFailureTest2() {
        //Given
        Long customerId = 1L;
        Customer mockCustomer = mock(Customer.class);
        Coupon coupon = TestCouponFactory.builder().build();
        CouponRedemption couponRedemption = CouponRedemption.of(coupon);

        given(couponRedemptionRepository.findByCouponCode(couponRedemption.getCouponCode()))
            .willReturn(Optional.of(couponRedemption));
        given(customerRepository.getById(customerId)).willReturn(mockCustomer);

        couponRedemptionService.allocateExistingCouponToCustomer(
            couponRedemption.getCouponCode(),
            customerId
        );

        //When Then
        assertThatThrownBy(() -> couponRedemptionService.allocateExistingCouponToCustomer(
            couponRedemption.getCouponCode(),
            customerId))
            .isInstanceOf(CouponRedemptionAlreadyAllocateCustomer.class)
            .hasMessageContaining(ErrorCode.COUPON_REDEMPTION_ALREADY_ALLOCATE.getMessage());
    }

    @Test
    @DisplayName("고객에게 쿠폰 자동 할당 - 성공 테스트")
    void allocateCouponToCustomerWithIssuanceSuccessTest() {
        //Given
        Long customerId = 1L;
        Long couponId = 1L;
        Customer mockCustomer = mock(Customer.class);
        Coupon spyCoupon = spy(TestCouponFactory.builder().build());
        ArgumentCaptor<CouponRedemption> CouponRedemptionCaptor = ArgumentCaptor.forClass(CouponRedemption.class);

        given(spyCoupon.getId()).willReturn(couponId);
        given(mockCustomer.getId()).willReturn(customerId);
        given(couponRepository.findById(couponId)).willReturn(Optional.of(spyCoupon));
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mockCustomer));

        //When
        couponRedemptionService.allocateCouponToCustomerWithIssuance(spyCoupon.getId(), customerId);

        //Then
        then(couponRedemptionRepository).should().save(CouponRedemptionCaptor.capture());
        CouponRedemption captorCouponRedemption = CouponRedemptionCaptor.getValue();
        assertThat(captorCouponRedemption.getCoupon().getId()).isEqualTo(couponId);
        assertThat(captorCouponRedemption.getCustomer().getId()).isEqualTo(customerId);
    }

    @Test
    @DisplayName("고객에게 쿠폰 자동 할당 - 실패 테스트 (잘못된 쿠폰 Id)")
    void allocateCouponToCustomerWithIssuanceFailureTest() {
        //Given
        Long invalidCouponId = 1L;
        Long customerId = 1L;

        given(couponRepository.findById(invalidCouponId)).willReturn(Optional.empty());

        //When Then
        assertThatThrownBy(() -> couponRedemptionService.allocateCouponToCustomerWithIssuance(
            invalidCouponId,
            customerId))
            .isInstanceOf(CouponNotFoundException.class)
            .hasMessageContaining(ErrorCode.COUPON_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("고객에게 쿠폰 자동 할당 - 실패 테스트 (잘못된 고객 Id)")
    void allocateCouponToCustomerWithIssuanceFailureTest2() {
        //Given
        Coupon spyCoupon = spy(TestCouponFactory.builder().build());
        Long invalidCustomerId = 1L;

        given(couponRepository.findById(any())).willReturn(Optional.of(spyCoupon));
        given(customerRepository.findById(invalidCustomerId)).willReturn(Optional.empty());

        //When Then
        assertThatThrownBy(() -> couponRedemptionService.allocateCouponToCustomerWithIssuance(
            spyCoupon.getId(),
            invalidCustomerId))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessageContaining(ErrorCode.CUSTOMER_NOT_FOUND.getMessage());
    }
}
