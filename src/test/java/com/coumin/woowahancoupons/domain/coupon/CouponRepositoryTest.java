package com.coumin.woowahancoupons.domain.coupon;

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

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
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
		Coupon coupon = createCoupons(IssuerType.ADMIN, 1L, 1).get(0);
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
		Coupon coupon = createCoupons(IssuerType.ADMIN, 1L, 1).get(0);

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
		List<Coupon> coupons1 = createCoupons(IssuerType.ADMIN, issuerId, 3);
		List<Coupon> coupons2 = createCoupons(IssuerType.STORE, 2L, 3);

		coupons1.forEach(coupon -> entityManager.persist(coupon));
		coupons2.forEach(coupon -> entityManager.persist(coupon));
		entityManager.clear();

		//When
		List<Coupon> allCouponsCreatedByAdmin = couponRepository.findByIssuerId(issuerId);

		//Then
		SoftAssertions.assertSoftly(softAssertions -> {
				softAssertions.assertThat(allCouponsCreatedByAdmin).hasSize(3);
			}
		);
	}

	private List<Coupon> createCoupons(IssuerType issuerType, long issuerId, int size) {

		ExpirationPolicy expirationPolicy = ExpirationPolicy.newByAfterIssueDate(14);

		return IntStream.range(0, size).mapToObj(i ->
			Coupon.builder(
				"testName#" + i,
				1000L * (i + 1),
				expirationPolicy,
				DiscountType.FIXED_AMOUNT,
				issuerType,
				issuerId
			).build()
		).collect(Collectors.toList());
	}
}
