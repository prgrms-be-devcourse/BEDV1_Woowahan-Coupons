package com.coumin.woowahancoupons.coupon.service;

import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveDto;
import com.coumin.woowahancoupons.coupon.dto.StoreCouponSaveRequestDto;
import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.store.Store;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SimpleCouponServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponService couponService;

    @Test
    @DisplayName("매장의 쿠폰 생성 성공")
    void saveAllStoreCouponsTest() {
        //Given
        Store store = createStore();
        List<StoreCouponSaveDto> storeCouponSaveDtos = IntStream.range(0, 5)
            .mapToObj(i -> StoreCouponSaveDto.builder()
                .name("name#" + i)
                .amount(1000L * (i + 1))
                .daysAfterIssuance(30)
                .minOrderPrice(1000L * (i + 1))
                .build())
            .collect(Collectors.toList());

        //When
        couponService.saveAllStoreCoupons(
            store.getId(),
            new StoreCouponSaveRequestDto(storeCouponSaveDtos));

        //Then
        List<Coupon> foundCoupons = couponRepository.findByIssuerId(store.getId());
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundCoupons).hasSize(5);
                softAssertions.assertThat(foundCoupons.get(0)).isNotNull();
                softAssertions.assertThat(foundCoupons.get(1)).isNotNull();
                softAssertions.assertThat(foundCoupons.get(2)).isNotNull();
                softAssertions.assertThat(foundCoupons.get(3)).isNotNull();
            }
        );
    }

    private Store createStore() {
        Store store = new Store("store#1");
        entityManager.persist(store);
        return store;
    }
}