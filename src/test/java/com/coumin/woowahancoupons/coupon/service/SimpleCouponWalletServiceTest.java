package com.coumin.woowahancoupons.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponWallet;
import com.coumin.woowahancoupons.domain.coupon.CouponWalletRepository;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.customer.CustomerRepository;
import com.coumin.woowahancoupons.global.error.ErrorCode;
import com.coumin.woowahancoupons.global.exception.CouponWalletNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleCouponWalletServiceTest {

    @Mock
    private CouponWalletRepository couponWalletRepository;
    
    @Mock
    private CustomerRepository customerRepository;
    
    @InjectMocks
    private SimpleCouponWalletService couponWalletService;

    @Test
    @DisplayName("고객이 쿠폰 코드를 입력해 쿠폰 발급 - 성공 테스트")
    void allocateCouponToCustomerSuccessTest() {
        //Given
        Long customerId = 1L;
        CouponWallet couponWallet = new CouponWallet(mock(Coupon.class));
        Customer mockCustomer = mock(Customer.class);
        given(couponWalletRepository.findById(any())).willReturn(Optional.of(couponWallet));
        given(customerRepository.getById(customerId)).willReturn(mockCustomer);
        given(mockCustomer.getId()).willReturn(customerId);
        //When
        assertThat(couponWallet.getCustomer()).isNull();
        couponWalletService.allocateCouponToCustomer(UUID.randomUUID(), customerId);
        //Then
        assertThat(couponWallet.getCustomer()).isNotNull();
        assertThat(couponWallet.getCustomer().getId()).isEqualTo(customerId);
    }

    @Test
    @DisplayName("고객이 쿠폰 코드를 입력해 쿠폰 발급 - 실패 테스트 (잘못된 쿠폰 Id)")
    void allocateCouponToCustomerFailureTest() {
        //Given
        given(couponWalletRepository.findById(any())).willReturn(Optional.empty());
        //When Then
        assertThatThrownBy(() -> couponWalletService.allocateCouponToCustomer(UUID.randomUUID(), 1L))
            .isInstanceOf(CouponWalletNotFoundException.class)
            .hasMessageContaining(ErrorCode.COUPON_WALLET_NOT_FOUND.getMessage());
    }
}
