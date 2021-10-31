package com.coumin.woowahancoupons.domain.coupon;

import com.coumin.woowahancoupons.coupon.TestCouponFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CouponRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("BaseEntity 생성 테스트")
    void baseEntityAuditingTest() {
        //Given
        LocalDateTime now = LocalDateTime.now().minusMinutes(1);
        Coupon coupon = TestCouponFactory.builder().build();
        entityManager.persist(coupon);
        entityManager.clear();

        //When
        Coupon foundCoupon = couponRepository.findById(coupon.getId()).get();

        //Then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundCoupon.getCreatedAt()).isAfter(now);
                softAssertions.assertThat(foundCoupon.getLastModifiedAt()).isAfter(now);
            }
        );
    }

    @Test
    @DisplayName("쿠폰 생성 테스트")
    void saveAllCouponTest() {
        //Given
        Coupon coupon = TestCouponFactory.builder().build();

        //When
        couponRepository.save(coupon);

        //Then
        List<Coupon> allCoupons = couponRepository.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(allCoupons).isNotEmpty();
                softAssertions.assertThat(allCoupons.get(0).getId()).isEqualTo(coupon.getId());
            }
        );
    }

    @Test
    @DisplayName("발행인의 아이디로 쿠폰 조회 테스트")
    void findCouponByIssuerIdTest() {
        //Given
        long issuerId = 1L;
        List<Coupon> createdCouponByAdmin1 = IntStream.range(0, 3)
            .mapToObj(i -> TestCouponFactory.builder()
                .issuerId(1L)
                .build())
            .collect(Collectors.toList());
        List<Coupon> createdCouponByAdmin2 = IntStream.range(0, 3)
            .mapToObj(i -> TestCouponFactory.builder()
                .issuerId(2L)
                .build())
            .collect(Collectors.toList());

        createdCouponByAdmin1.forEach(coupon -> entityManager.persist(coupon));
        createdCouponByAdmin2.forEach(coupon -> entityManager.persist(coupon));
        entityManager.clear();

        //When
        List<Coupon> foundCoupons = couponRepository.findByIssuerId(issuerId);

        //Then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundCoupons)
                    .isNotEmpty()
                    .hasSize(3);
            }
        );
    }
}
