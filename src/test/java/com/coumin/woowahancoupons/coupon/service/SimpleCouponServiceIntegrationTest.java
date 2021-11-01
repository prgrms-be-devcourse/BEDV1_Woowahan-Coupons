package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.TestCouponFactory;
import com.coumin.woowahancoupons.coupon.dto.CouponConverter;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateRequestDto;
import com.coumin.woowahancoupons.coupon.dto.CouponCreateResponseDto;
import com.coumin.woowahancoupons.domain.coupon.CouponAdmin;
import com.coumin.woowahancoupons.domain.coupon.CouponAdminRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class SimpleCouponServiceIntegrationTest {

    @Autowired
    CouponAdminRepository couponAdminRepository;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponConverter couponConverter;


    @Test
    @DisplayName("관리자가 쿠폰 생성(발행) 성공")
    void generateCouponTest() {
        //Given
        CouponAdmin couponAdmin = new CouponAdmin("Admin_WOOCOU");

        Long couponAdminId = couponAdminRepository.save(couponAdmin).getId();

        CouponCreateRequestDto couponCreateRequestDto = couponConverter
            .convertToCouponCreateRequest(
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
        CouponCreateResponseDto couponCreateResponseDto = couponService.generateCoupon(
            couponCreateRequestDto, couponAdminId);

        //Then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(couponCreateRequestDto.getName())
                .isEqualTo(couponCreateResponseDto.getName());
            soft.assertThat(couponCreateRequestDto.getAmount())
                .isEqualTo(couponCreateResponseDto.getAmount());
            soft.assertThat(couponCreateRequestDto.getMinOrderPrice())
                .isEqualTo(couponCreateResponseDto.getMinOrderPrice());
            soft.assertThat(couponCreateRequestDto.getDiscountType())
                .isEqualTo(couponCreateResponseDto.getDiscountType());
            soft.assertThat(couponCreateRequestDto.getIssuerType())
                .isEqualTo(couponCreateResponseDto.getIssuerType());
        });

    }
}
