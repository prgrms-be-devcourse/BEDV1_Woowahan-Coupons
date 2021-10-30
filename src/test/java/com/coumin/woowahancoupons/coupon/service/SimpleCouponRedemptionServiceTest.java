package com.coumin.woowahancoupons.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.spy;

import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemptionRepository;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.customer.CustomerRepository;
import com.coumin.woowahancoupons.global.error.ErrorCode;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionAlreadyAllocateCustomer;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleCouponRedemptionServiceTest {

    @Mock
    private CouponRedemptionRepository couponRedemptionRepository;
    
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Coupon coupon;

    @Mock
    private Customer customer;

    @Mock
    private ExpirationPolicy expirationPolicy;
    
    @InjectMocks
    private SimpleCouponRedemptionService couponRedemptionService;

    @Test
    @DisplayName("고객이 쿠폰 코드를 입력해 쿠폰 발급 - 성공 테스트")
    void allocateCouponToCustomerSuccessTest() {
        //Given
        given(coupon.getExpirationPolicy()).willReturn(expirationPolicy);
        CouponRedemption couponRedemption = new CouponRedemption(coupon);

        UUID issuanceCode = UUID.randomUUID();
        given(couponRedemptionRepository.findByIssuanceCode(issuanceCode)).willReturn(Optional.of(couponRedemption));

        Long customerId = 1L;
        given(customerRepository.getById(customerId)).willReturn(customer);
        given(customer.getId()).willReturn(customerId);

        //When
        assertThat(couponRedemption.getCustomer()).isNull();
        couponRedemptionService.allocateCouponToCustomer(issuanceCode, customerId);

        //Then
        assertThat(couponRedemption.getCustomer()).isNotNull();
        assertThat(couponRedemption.getCustomer().getId()).isEqualTo(customerId);
    }

    @Test
    @DisplayName("고객이 쿠폰 코드를 입력해 쿠폰 발급 - 실패 테스트 (잘못된 쿠폰 Id)")
    void allocateCouponToCustomerCodeFailureTest() {
        //Given
        UUID invalidId = UUID.randomUUID();
        given(couponRedemptionRepository.findByIssuanceCode(invalidId)).willReturn(Optional.empty());

        //When Then
        assertThatThrownBy(() -> couponRedemptionService.allocateCouponToCustomer(invalidId, 1L))
            .isInstanceOf(CouponRedemptionNotFoundException.class)
            .hasMessageContaining(ErrorCode.COUPON_REDEMPTION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("고객이 쿠폰 코드를 입력해 쿠폰 발급 - 실패 테스트 (이미 고객이 할당된 쿠폰)")
    void allocateCouponToCustomerCustomerFailureTest() {
        //Given
        given(coupon.getExpirationPolicy()).willReturn(expirationPolicy);
        CouponRedemption couponRedemption = spy(new CouponRedemption(coupon));

        UUID issuanceCode = UUID.randomUUID();
        given(couponRedemptionRepository.findByIssuanceCode(issuanceCode)).willReturn(Optional.of(couponRedemption));

        Long customerId = 1L;
        given(customerRepository.getById(customerId)).willReturn(customer);
        given(couponRedemption.getCustomer()).willReturn(customer);
        willThrow(new CouponRedemptionAlreadyAllocateCustomer()).given(couponRedemption).allocateCustomer(customer);

        //When Then
        assertThat(couponRedemption.getCustomer()).isNotNull();
        assertThatThrownBy(() -> couponRedemptionService.allocateCouponToCustomer(issuanceCode, customerId))
            .isInstanceOf(CouponRedemptionAlreadyAllocateCustomer.class)
            .hasMessageContaining(ErrorCode.COUPON_REDEMPTION_ALREADY_ALLOCATE.getMessage());
    }
}
