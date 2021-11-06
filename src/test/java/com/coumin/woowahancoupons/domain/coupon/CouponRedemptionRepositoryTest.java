package com.coumin.woowahancoupons.domain.coupon;


import com.coumin.woowahancoupons.coupon.factory.TestCouponFactory;
import com.coumin.woowahancoupons.domain.customer.Customer;
import java.time.LocalDateTime;
import java.util.List;
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
        entityManager.persist(coupon);
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
    @DisplayName("쿠폰 코드로 조회 성공")
    void findCouponRedemptionByIssuanceCodeTest() {
        //Given
        Coupon coupon = TestCouponFactory.builder().build();
        entityManager.persist(coupon);
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

    @Test
    @DisplayName("고객으로 아직 사용하지 않은 쿠폰리뎀션 리스트 조회")
    void findByCustomerAndUsedNotTest() {
        //Given
        Coupon coupon = TestCouponFactory.builder().build();
        entityManager.persist(coupon);
        Customer customer = new Customer("tester");
        entityManager.persist(customer);

        CouponRedemption couponRedemption1 = CouponRedemption.of(coupon, customer);
        entityManager.persist(couponRedemption1);
        CouponRedemption couponRedemption2 = CouponRedemption.of(coupon, customer);
        entityManager.persist(couponRedemption2);
        CouponRedemption couponRedemption3 = CouponRedemption.of(coupon, customer);
        couponRedemption3.use();
        entityManager.persist(couponRedemption3);
        entityManager.clear();

        //When
        List<CouponRedemption> foundCouponRedemption = couponRedemptionRepository
            .findByCustomerIdAndUsedFalse(customer.getId());
        foundCouponRedemption.get(0).getCoupon().getMinOrderPrice(); // n+1 check

        //Then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundCouponRedemption)
                    .isNotEmpty()
                    .hasSize(2);
                foundCouponRedemption.forEach(couponRedemption -> {
                        softAssertions.assertThat(couponRedemption.isUsed()).isEqualTo(false);
                    }
                );
            }
        );
    }
}
