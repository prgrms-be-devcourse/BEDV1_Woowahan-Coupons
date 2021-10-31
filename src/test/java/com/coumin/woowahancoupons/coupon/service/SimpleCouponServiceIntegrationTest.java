package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.TestCouponFactory;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateConverter;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponseDto;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimpleCouponServiceIntegrationTest {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponService couponService;

    @Test
    @DisplayName("쿠폰 생성(발행) 성공")
    void generateCouponTest() {
        //Given
        CouponCreateConverter couponCreateConverter = new CouponCreateConverter();
        CouponCreateRequestDto couponCreateRequestDto = couponCreateConverter.convertToCouponCreateRequest(
            TestCouponFactory.builder()
                .name("기리보이가 쏜다")
                .amount(10_000L)
                .minOrderPrice(10_000L)
                .maxCount(10)
                .allocatedCount(10)
                .promotionCode("프로모션코드")
                .build()
        );

        //When
        CouponCreateResponseDto couponCreateResponseDto = couponService.generateCoupon(couponCreateRequestDto);

        //Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(couponCreateRequestDto.getName()).isEqualTo(couponCreateResponseDto.getName());
            softAssertions.assertThat(couponCreateRequestDto.getAmount()).isEqualTo(couponCreateResponseDto.getAmount());
            softAssertions.assertThat(couponCreateRequestDto.getMinOrderPrice()).isEqualTo(couponCreateResponseDto.getMinOrderPrice());
            softAssertions.assertThat(couponCreateRequestDto.getDiscountType()).isEqualTo(couponCreateResponseDto.getDiscountType());
            softAssertions.assertThat(couponCreateRequestDto.getIssuerType()).isEqualTo(couponCreateResponseDto.getIssuerType());
        });
    }
}
