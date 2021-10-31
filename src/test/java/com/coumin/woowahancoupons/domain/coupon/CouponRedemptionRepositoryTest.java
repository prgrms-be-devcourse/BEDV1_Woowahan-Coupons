package com.coumin.woowahancoupons.domain.coupon;


import com.coumin.woowahancoupons.coupon.TestCouponFactory;
import java.time.LocalDateTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CouponRedemptionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CouponRedemptionRepository couponRedemptionRepository;

    @Test
    @DisplayName("BaseEntity 생성 테스트")
    void baseEntityAuditingTest() {
        //Given
        LocalDateTime now = LocalDateTime.now().minusMinutes(1);
        Coupon coupon = TestCouponFactory.builder().build();
        CouponRedemption couponRedemption = CouponRedemption.of(coupon);

        //When
        CouponRedemption foundCouponRedemption = couponRedemptionRepository.save(couponRedemption);

        //Then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundCouponRedemption.getCreatedAt()).isAfter(now);
                softAssertions.assertThat(foundCouponRedemption.getLastModifiedAt()).isAfter(now);
            }
        );
    }

    @Test
    @DisplayName("발급 코드로 CouponRedemption 조회 성공")
    void findCouponRedemptionByIssuanceCodeTest() {
        //Given
        Coupon coupon = TestCouponFactory.builder().build();
        CouponRedemption couponRedemption = CouponRedemption.of(coupon);
        entityManager.persist(couponRedemption);

        //When
        CouponRedemption foundCouponRedemption = couponRedemptionRepository
            .findByCouponCode(couponRedemption.getCouponCode()).get();

        //Then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundCouponRedemption.getId())
                    .isEqualTo(couponRedemption.getId());
            }
        );
    }
}