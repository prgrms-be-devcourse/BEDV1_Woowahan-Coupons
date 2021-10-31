package com.coumin.woowahancoupons.domain.coupon;

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
		CouponRedemption couponRedemption = new CouponRedemption(createCoupon());

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
		Coupon coupon = createCoupon();
		CouponRedemption couponRedemption = new CouponRedemption(createCoupon());
		entityManager.persist(couponRedemption);

		//When
		CouponRedemption foundCouponRedemption = couponRedemptionRepository
			.findByIssuanceCode(couponRedemption.getIssuanceCode()).get();

		//Then
		SoftAssertions.assertSoftly(softAssertions -> {
				softAssertions.assertThat(foundCouponRedemption.getId())
					.isEqualTo(couponRedemption.getId());
			}
		);
	}

	private Coupon createCoupon() {
		Coupon coupon = Coupon.builder(
			"testName#1",
			1000L,
			ExpirationPolicy.newByAfterIssueDate(14),
			DiscountType.FIXED_AMOUNT,
			IssuerType.ADMIN,
			1L
		).build();
		entityManager.persist(coupon);

		return coupon;
	}
}