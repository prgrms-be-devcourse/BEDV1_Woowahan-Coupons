package com.coumin.woowahancoupons.coupon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.coumin.woowahancoupons.coupon.converter.CouponRedemptionConverter;
import com.coumin.woowahancoupons.coupon.dto.CouponCheckRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponRedemptionResponseDto;
import com.coumin.woowahancoupons.coupon.factory.TestCouponFactory;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemption;
import com.coumin.woowahancoupons.domain.coupon.CouponRedemptionRepository;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import com.coumin.woowahancoupons.domain.customer.Customer;
import com.coumin.woowahancoupons.domain.customer.CustomerRepository;
import com.coumin.woowahancoupons.domain.store.Brand;
import com.coumin.woowahancoupons.domain.store.Store;
import com.coumin.woowahancoupons.domain.store.StoreRepository;
import com.coumin.woowahancoupons.global.error.ErrorCode;
import com.coumin.woowahancoupons.global.exception.CouponAlreadyUseException;
import com.coumin.woowahancoupons.global.exception.CouponIssuerIdNotMatchException;
import com.coumin.woowahancoupons.global.exception.CouponMaxCountOverException;
import com.coumin.woowahancoupons.global.exception.CouponMinOrderPriceNotSatisfyException;
import com.coumin.woowahancoupons.global.exception.CouponNotFoundException;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionAlreadyAllocateCustomer;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionExpireException;
import com.coumin.woowahancoupons.global.exception.CouponRedemptionNotFoundException;
import com.coumin.woowahancoupons.global.exception.CustomerNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleCouponRedemptionServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponRedemptionRepository couponRedemptionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CouponRedemptionConverter couponRedemptionConverter;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private SimpleCouponRedemptionService couponRedemptionService;

    @Test
    @DisplayName("????????? ?????? ????????? ????????? ?????? ?????? - ?????? ?????????")
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
    @DisplayName("????????? ?????? ????????? ????????? ?????? ?????? - ?????? ????????? (????????? ?????? ??????)")
    void allocateExistingCouponToCustomerFailureTest() {
        //Given
        UUID invalidCouponCode = UUID.randomUUID();
        given(couponRedemptionRepository.findByCouponCode(invalidCouponCode))
            .willReturn(Optional.empty());

        //When Then
        assertThatThrownBy(
            () -> couponRedemptionService.allocateExistingCouponToCustomer(invalidCouponCode, 1L))
            .isInstanceOf(CouponRedemptionNotFoundException.class)
            .hasMessageContaining(ErrorCode.COUPON_REDEMPTION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????? ?????? ?????? - ?????? ????????? (?????? ????????? ??????)")
    void allocateExistingCouponToCustomerFailureTest2() {
        //Given
        Long customerId = 1L;
        Customer mockCustomer = mock(Customer.class);
        Coupon coupon = TestCouponFactory.builder().build();
        CouponRedemption couponRedemption = CouponRedemption.of(coupon);

        UUID couponCode = couponRedemption.getCouponCode();
        given(couponRedemptionRepository.findByCouponCode(couponCode))
            .willReturn(Optional.of(couponRedemption));
        given(customerRepository.getById(customerId)).willReturn(mockCustomer);

        couponRedemptionService.allocateExistingCouponToCustomer(
            couponCode,
            customerId
        );

        //When Then
        assertThatThrownBy(() -> couponRedemptionService.allocateExistingCouponToCustomer(
            couponCode,
            customerId))
            .isInstanceOf(CouponRedemptionAlreadyAllocateCustomer.class)
            .hasMessageContaining(ErrorCode.COUPON_REDEMPTION_ALREADY_ALLOCATE.getMessage());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????? - ?????? ?????????")
    void allocateCouponToCustomerWithIssuanceSuccessTest() {
        //Given
        Long customerId = 1L;
        Long couponId = 1L;
        Customer mockCustomer = mock(Customer.class);
        Coupon spyCoupon = spy(TestCouponFactory.builder().build());
        ArgumentCaptor<CouponRedemption> CouponRedemptionCaptor = ArgumentCaptor
            .forClass(CouponRedemption.class);

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
    @DisplayName("???????????? ?????? ?????? ?????? - ?????? ????????? (????????? ?????? Id)")
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
    @DisplayName("???????????? ?????? ?????? ?????? - ?????? ????????? (????????? ?????? Id)")
    void allocateCouponToCustomerWithIssuanceFailureTest2() {
        //Given
        Coupon spyCoupon = spy(TestCouponFactory.builder().build());
        Long couponId = spyCoupon.getId();
        Long invalidCustomerId = 1L;

        given(couponRepository.findById(any())).willReturn(Optional.of(spyCoupon));
        given(customerRepository.findById(invalidCustomerId)).willReturn(Optional.empty());

        //When Then
        assertThatThrownBy(() -> couponRedemptionService.allocateCouponToCustomerWithIssuance(
            couponId,
            invalidCustomerId))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessageContaining(ErrorCode.CUSTOMER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("????????? ??????????????? ?????? ????????? ????????? ??????")
    void findCustomerCouponRedemptionsTest() {
        //Given
        Long customerId = 1L;
        Customer mockCustomer = mock(Customer.class);
        Coupon spyCoupon = spy(TestCouponFactory.builder().build());
        List<CouponRedemption> couponRedemptions = LongStream.range(0, 5)
            .mapToObj(seq -> CouponRedemption.of(spyCoupon, mockCustomer))
            .collect(Collectors.toList());

        given(couponRedemptionRepository.findByCustomerIdAndUsedFalse(customerId))
            .willReturn(couponRedemptions);
        given(couponRedemptionConverter.convertToCouponRedemptionResponseDto(
            any(CouponRedemption.class)
        )).willReturn(mock(CouponRedemptionResponseDto.class));

        //When
        List<CouponRedemptionResponseDto> couponRedemptionResponseDtoList = couponRedemptionService
            .findCustomerCouponRedemptions(customerId);

        //Then
        assertThat(couponRedemptionResponseDtoList).hasSize(5);
        verify(couponRedemptionRepository, only()).findByCustomerIdAndUsedFalse(customerId);
    }

    @DisplayName("?????? ?????? ?????? - ?????? ?????????")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100})
    void issueCouponCodesSuccessTest(int issuanceCount) {
        //Given
        Long couponId = 1L;
        Coupon spyCoupon = spy(TestCouponFactory.builder().maxCount(issuanceCount + 1).build());
        ArrayList mockList = mock(ArrayList.class);
        given(couponRepository.findByIdForUpdate(couponId)).willReturn(Optional.of(spyCoupon));
        given(couponRedemptionRepository.saveAll(any())).willReturn(mockList);
        given(mockList.size()).willReturn(issuanceCount);

        //When
        assertThat(spyCoupon.getAllocatedCount()).isZero();
        couponRedemptionService.issueCouponCodes(couponId, issuanceCount);

        //Then
        assertThat(spyCoupon.getAllocatedCount()).isEqualTo(issuanceCount);
    }

    @DisplayName("?????? ?????? ?????? - ?????? ????????? (?????? ?????? ????????? ?????? ?????? ?????? ?????? ?????? ????????? ??????)")
    @ParameterizedTest
    @ValueSource(ints = {10, 100})
    void issueCouponCodesFailureTest(int issuanceCount) {
        //Given
        Long couponId = 1L;
        Coupon spyCoupon = spy(TestCouponFactory.builder().maxCount(issuanceCount - 1).build());
        given(couponRepository.findByIdForUpdate(couponId)).willReturn(Optional.of(spyCoupon));

        //When Then
        assertThatThrownBy(() -> couponRedemptionService.issueCouponCodes(couponId, issuanceCount))
            .isInstanceOf(CouponMaxCountOverException.class);
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ?????????")
    void useCustomerCouponSuccessTest() {
        //Given
        try(MockedStatic<LocalDateTime> localDateTimeMockedStatic = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS)) {
            Long couponRedemptionId = 1L;
            LocalDateTime expectedUsedTime = LocalDateTime.of(2021, 11, 3, 17, 45, 30);
            Coupon spyCoupon = spy(TestCouponFactory.builder().build());
            Customer mockCustomer = mock(Customer.class);
            CouponRedemption spyCouponRedemption = spy(
                CouponRedemption.of(spyCoupon, mockCustomer));

            localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(expectedUsedTime);
            given(couponRedemptionRepository.findById(couponRedemptionId))
                .willReturn(Optional.of(spyCouponRedemption));

            //When
            couponRedemptionService.useCustomerCoupon(couponRedemptionId);

            //Then
            verify(couponRedemptionRepository, times(1)).findById(couponRedemptionId);
            verify(spyCouponRedemption, times(1)).use();
            SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(spyCouponRedemption.isUsed()).isEqualTo(true);
                    softAssertions.assertThat(spyCouponRedemption.getUsedAt()).isAfter(LocalDateTime.of(2021, 11, 3, 17, 45, 29));
                    softAssertions.assertThat(spyCouponRedemption.getUsedAt()).isEqualTo(expectedUsedTime);
                    softAssertions.assertThat(spyCouponRedemption.getUsedAt()).isBefore(LocalDateTime.of(2021, 11, 3, 17, 45, 31));
                }
            );
        }

    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ?????????(???????????? ?????? ??????)")
    void useCustomerCouponFailureTest() {
        //Given
        Long couponRedemptionId = 1L;
        Coupon spyCoupon = spy(TestCouponFactory.builder().build());
        Customer mockCustomer = mock(Customer.class);
        CouponRedemption spyCouponRedemption = spy(CouponRedemption.of(spyCoupon, mockCustomer));
        given(couponRedemptionRepository.findById(couponRedemptionId))
            .willReturn(Optional.empty());

        //When, Then
        assertThatThrownBy(() -> couponRedemptionService.useCustomerCoupon(couponRedemptionId))
            .isInstanceOf(CouponRedemptionNotFoundException.class)
            .hasMessageContaining(ErrorCode.COUPON_REDEMPTION_NOT_FOUND.getMessage());

        verify(couponRedemptionRepository, times(1)).findById(couponRedemptionId);
        verify(spyCouponRedemption, never()).use();
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(spyCouponRedemption.isUsed()).isEqualTo(false);
                softAssertions.assertThat(spyCouponRedemption.getUsedAt()).isNull();
            }
        );
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ?????????(?????? ????????? ??????)")
    void useCustomerCouponFailureTest2() {
        //Given
        Long couponRedemptionId = 1L;
        Coupon spyCoupon = spy(TestCouponFactory.builder().build());
        Customer mockCustomer = mock(Customer.class);
        CouponRedemption spyCouponRedemption = spy(CouponRedemption.of(spyCoupon, mockCustomer));
        given(couponRedemptionRepository.findById(couponRedemptionId))
            .willReturn(Optional.of(spyCouponRedemption));
        willThrow(new CouponAlreadyUseException()).given(spyCouponRedemption).use();

        //When, Then
        assertThatThrownBy(() -> couponRedemptionService.useCustomerCoupon(couponRedemptionId))
            .isInstanceOf(CouponAlreadyUseException.class)
            .hasMessageContaining(ErrorCode.COUPON_REDEMPTION_ALREADY_USE.getMessage());

        verify(couponRedemptionRepository, times(1)).findById(couponRedemptionId);
        verify(spyCouponRedemption, times(1)).use();
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(spyCouponRedemption.isUsed()).isEqualTo(false);
                softAssertions.assertThat(spyCouponRedemption.getUsedAt()).isNull();
            }
        );
    }

    @Test
    @DisplayName("???????????? ?????? ?????? - ?????? ?????????(?????? ????????? ????????? ??????)")
    void useCustomerCouponFailureTest3() {
        //Given
        Long couponRedemptionId = 1L;
        Coupon spyCoupon = spy(TestCouponFactory.builder().build());
        Customer mockCustomer = mock(Customer.class);
        CouponRedemption spyCouponRedemption = spy(CouponRedemption.of(spyCoupon, mockCustomer));
        given(couponRedemptionRepository.findById(couponRedemptionId))
            .willReturn(Optional.of(spyCouponRedemption));
        willThrow(new CouponRedemptionExpireException()).given(spyCouponRedemption).use();

        //When, Then
        assertThatThrownBy(() -> couponRedemptionService.useCustomerCoupon(couponRedemptionId))
            .isInstanceOf(CouponRedemptionExpireException.class)
            .hasMessageContaining(ErrorCode.COUPON_REDEMPTION_EXPIRE.getMessage());

        verify(couponRedemptionRepository, times(1)).findById(couponRedemptionId);
        verify(spyCouponRedemption, times(1)).use();
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(spyCouponRedemption.isUsed()).isEqualTo(false);
                softAssertions.assertThat(spyCouponRedemption.getUsedAt()).isNull();
            }
        );
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? - ?????? ?????? ????????? (admin Type)")
    void checkForUseSuccessTest() {
        //Given
        long couponAdminId = 1L;
        long storeId = 2L;
        long orderPrice = 10000L;
        long couponRedemptionId = 3L;

        Coupon spyCoupon = spy(TestCouponFactory.builder()
            .minOrderPrice(orderPrice)
            .issuerType(IssuerType.ADMIN)
            .issuerId(couponAdminId)
            .build());
        CouponRedemption spyCouponRedemption = spy(CouponRedemption.of(spyCoupon, mock(Customer.class)));
        CouponCheckRequestDto couponCheckRequestDto = mock(CouponCheckRequestDto.class);

        given(couponRedemptionRepository.findById(couponRedemptionId))
            .willReturn(Optional.of(spyCouponRedemption));
        given(couponCheckRequestDto.getStoreId()).willReturn(storeId);
        given(couponCheckRequestDto.getOrderPrice()).willReturn(orderPrice);

        //When
        couponRedemptionService.checkCouponForUse(couponRedemptionId, couponCheckRequestDto);

        //Then
        assertThat(spyCouponRedemption.isBrandCouponRedemption()).isFalse();
        verify(couponRedemptionRepository, times(1)).findById(couponRedemptionId);
        verify(storeRepository, never()).findById(storeId);
        verify(spyCouponRedemption, times(1)).verifyForUse(storeId, orderPrice);
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? - ?????? ?????? ????????? (store Type)")
    void checkForUseSuccessTest2() {
        //Given
        long storeId = 1L;
        long orderPrice = 10000L;
        long couponRedemptionId = 2L;

        Coupon spyCoupon = spy(TestCouponFactory.builder()
            .issuerType(IssuerType.STORE)
            .issuerId(storeId)
            .build());
        CouponRedemption spyCouponRedemption = spy(CouponRedemption.of(spyCoupon, mock(Customer.class)));
        CouponCheckRequestDto couponCheckRequestDto = mock(CouponCheckRequestDto.class);

        given(couponRedemptionRepository.findById(couponRedemptionId))
            .willReturn(Optional.of(spyCouponRedemption));
        given(couponCheckRequestDto.getStoreId()).willReturn(storeId);
        given(couponCheckRequestDto.getOrderPrice()).willReturn(orderPrice);

        //When
        couponRedemptionService.checkCouponForUse(couponRedemptionId, couponCheckRequestDto);

        //Then
        assertThat(spyCouponRedemption.isBrandCouponRedemption()).isFalse();
        verify(couponRedemptionRepository, times(1)).findById(couponRedemptionId);
        verify(storeRepository, never()).findById(storeId);
        verify(spyCouponRedemption, times(1)).verifyForUse(storeId, orderPrice);
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? - ?????? ?????? ????????? (brand Type)")
    void checkForUseSuccessTest3() {
        //Given
        long brandId = 1L;
        long storeId = 2L;
        long orderPrice = 10000L;
        long couponRedemptionId = 3L;

        Brand mockBrand = mock(Brand.class);
        Store mockStore = mock(Store.class);
        Coupon spyCoupon = spy(TestCouponFactory.builder()
            .minOrderPrice(orderPrice)
            .issuerType(IssuerType.BRAND)
            .issuerId(brandId)
            .build());
        CouponRedemption spyCouponRedemption = spy(CouponRedemption.of(spyCoupon, mock(Customer.class)));
        CouponCheckRequestDto couponCheckRequestDto = mock(CouponCheckRequestDto.class);

        given(couponRedemptionRepository.findById(couponRedemptionId))
            .willReturn(Optional.of(spyCouponRedemption));
        given(couponCheckRequestDto.getStoreId()).willReturn(storeId);
        given(couponCheckRequestDto.getOrderPrice()).willReturn(orderPrice);
        given(storeRepository.findById(storeId)).willReturn(Optional.of(mockStore));
        given(mockStore.getBrand()).willReturn(mockBrand);
        given(mockBrand.getId()).willReturn(brandId);

        //When
        couponRedemptionService.checkCouponForUse(couponRedemptionId, couponCheckRequestDto);

        //Then
        assertThat(spyCouponRedemption.isBrandCouponRedemption()).isTrue();
        verify(couponRedemptionRepository, times(1)).findById(couponRedemptionId);
        verify(storeRepository, times(1)).findById(storeId);
        verify(spyCouponRedemption, times(1)).verifyForUse(brandId, orderPrice);
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? - ?????? ????????? (???????????? ?????? ??????)")
    void checkForUseFailureTest() {
        //Given
        long couponAdminId = 1L;
        long storeId = 2L;
        long orderPrice = 10000L;
        long couponRedemptionId = 3L;

        Coupon spyCoupon = spy(TestCouponFactory.builder()
            .minOrderPrice(20000L)
            .issuerType(IssuerType.ADMIN)
            .issuerId(couponAdminId)
            .build());
        CouponRedemption spyCouponRedemption = spy(CouponRedemption.of(spyCoupon, mock(Customer.class)));
        CouponCheckRequestDto couponCheckRequestDto = mock(CouponCheckRequestDto.class);

        given(couponRedemptionRepository.findById(couponRedemptionId))
            .willReturn(Optional.of(spyCouponRedemption));
        given(couponCheckRequestDto.getStoreId()).willReturn(storeId);
        given(couponCheckRequestDto.getOrderPrice()).willReturn(orderPrice);

        //When, Then
        assertThatThrownBy(() -> couponRedemptionService
            .checkCouponForUse(couponRedemptionId, couponCheckRequestDto))
            .isInstanceOf(CouponMinOrderPriceNotSatisfyException.class)
            .hasMessageContaining(String.format(
                ErrorCode.COUPON_MIN_ORDER_PRICE_NOT_SATISFY.getMessage(), 20000L));
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? - ?????? ????????? (?????? ????????? ????????? ???????????? ??????)")
    void checkForUseFailureTest2() {
        //Given
        long brandId = 1L;
        long storeId = 2L;
        long orderPrice = 10000L;
        long couponRedemptionId = 3L;
        long otherBrandId = 4L;

        Brand mockBrand = mock(Brand.class);
        Store mockStore = mock(Store.class);
        Coupon spyCoupon = spy(TestCouponFactory.builder()
            .minOrderPrice(orderPrice)
            .issuerType(IssuerType.BRAND)
            .issuerId(brandId)
            .build());
        CouponRedemption spyCouponRedemption = spy(CouponRedemption.of(spyCoupon, mock(Customer.class)));
        CouponCheckRequestDto couponCheckRequestDto = mock(CouponCheckRequestDto.class);

        given(couponRedemptionRepository.findById(couponRedemptionId))
            .willReturn(Optional.of(spyCouponRedemption));
        given(couponCheckRequestDto.getStoreId()).willReturn(storeId);
        given(couponCheckRequestDto.getOrderPrice()).willReturn(orderPrice);
        given(storeRepository.findById(storeId)).willReturn(Optional.of(mockStore));
        given(mockStore.getBrand()).willReturn(mockBrand);
        given(mockBrand.getId()).willReturn(otherBrandId);

        //When, Then
        assertThatThrownBy(() -> couponRedemptionService
            .checkCouponForUse(couponRedemptionId, couponCheckRequestDto))
            .isInstanceOf(CouponIssuerIdNotMatchException.class)
            .hasMessageContaining(String.format(
                ErrorCode.COUPON_ISSUER_ID_NOT_MATCH.getMessage(),
                IssuerType.BRAND,
                otherBrandId)
            );
    }
}
