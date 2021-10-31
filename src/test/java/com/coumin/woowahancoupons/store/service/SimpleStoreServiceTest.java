package com.coumin.woowahancoupons.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import com.coumin.woowahancoupons.coupon.TestCouponFactory;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponResponseDto;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimpleStoreServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private SimpleStoreService simpleStoreService;

    @Test
    @DisplayName("매장의 쿠폰 리스트 조회 서비스 성공")
    void findStoreCouponsTest() {
        //Given
        long storeId = 1L;
        List<Coupon> storeCoupons = IntStream.range(0, 3)
            .mapToObj(i -> TestCouponFactory.builder()
                .id((long) i)
                .name("store coupon#" + i)
                .amount(1000L * (i + 1))
                .expirationPolicy(ExpirationPolicy.newByAfterIssueDate(14))
                .discountType(DiscountType.FIXED_AMOUNT)
                .issuerType(IssuerType.STORE)
                .issuerId(storeId)
                .build())
            .collect(Collectors.toList());

        given(couponRepository.findByIssuerId(storeId)).willReturn(storeCoupons);

        //When
        List<StoreCouponResponseDto> responseDtoList = simpleStoreService.findStoreCoupons(storeId);

        //Then
        assertThat(responseDtoList).hasSize(storeCoupons.size());
        verify(couponRepository, only()).findByIssuerId(storeId);
    }
}
