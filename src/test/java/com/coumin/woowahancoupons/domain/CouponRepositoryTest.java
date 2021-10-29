package com.coumin.woowahancoupons.domain;

import com.coumin.woowahancoupons.domain.coupon.Coupon;
import com.coumin.woowahancoupons.domain.coupon.CouponRepository;
import com.coumin.woowahancoupons.domain.coupon.DiscountType;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicy;
import com.coumin.woowahancoupons.domain.coupon.ExpirationPolicyType;
import com.coumin.woowahancoupons.domain.coupon.IssuerType;
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
		LocalDateTime now = LocalDateTime.now();
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
		long adminId = 1L;
		List<Coupon> coupons1 = createCoupons(IssuerType.ADMIN, adminId, 3);
		List<Coupon> coupons2 = createCoupons(IssuerType.STORE, 2L, 3);

		coupons1.forEach(coupon -> entityManager.persist(coupon));
		coupons2.forEach(coupon -> entityManager.persist(coupon));
		entityManager.clear();

		//When
		List<Coupon> allCouponsCreatedByAdmin = couponRepository.findByIssuerId(adminId);

		//Then
		SoftAssertions.assertSoftly(softAssertions -> {
				softAssertions.assertThat(allCouponsCreatedByAdmin).hasSize(3);
			}
		);
	}

	private List<Coupon> createCoupons(IssuerType issuerType, long issuerId, int size) {

		ExpirationPolicy expirationPolicy = ExpirationPolicy.ByAfterIssueDateTypeBuilder()
			.expirationPolicyType(ExpirationPolicyType.AFTER_ISSUE_DATE)
			.daysFromIssuance(14)
			.build();

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
