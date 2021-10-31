package com.coumin.woowahancoupons.domain.coupon;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

	List<Coupon> findByIssuerId(Long issuerId);
}
